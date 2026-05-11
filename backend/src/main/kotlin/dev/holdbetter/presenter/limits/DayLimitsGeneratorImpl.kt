package dev.holdbetter.presenter.limits

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.interactor.DayLimitsGenerator
import dev.holdbetter.interactor.DayLimitsGenerator.Config.SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS
import dev.holdbetter.interactor.DayLimitsGenerator.Config.SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS
import dev.holdbetter.interactor.DayLimitsGenerator.Config.SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES
import dev.holdbetter.isLeapYear
import dev.holdbetter.presenter.MatchesGroupedByDayAndSeasonalMonth
import dev.holdbetter.presenter.MonthAndYear
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

typealias DayLimitMap = Map<LocalDate, DayLimit>
typealias MutableDayLimitMap = LinkedHashMap<LocalDate, DayLimit>

internal class DayLimitsGeneratorImpl : DayLimitsGenerator {

    override suspend fun generateDayLimits(
        groupedMatches: MatchesGroupedByDayAndSeasonalMonth,
        limitsStore: GetRemainedMonthLimitUseCase,
        leapYear: Boolean
    ): DayLimitMap {
        val dayLimitMap: MutableDayLimitMap = linkedMapOf()
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val hasMatchesThisMonth = groupedMatches.containsKey(today.monthNumber to today.year)

        groupedMatches.forEach { monthAndMatchesByDay ->
            val (month, year) = monthAndMatchesByDay.key
            val nonMatchDayCount = countNotMatchDays(monthAndMatchesByDay, today, leapYear)
            val remainedLimit = limitsStore.getRemainedMonthLimit(month, year) - nonMatchDayCount

            val monthAndMatches = monthAndMatchesByDay.value.mapValues { playDay ->
                val firstMatchStart = playDay.value.first().startDate!!
                val firstMatchDate = firstMatchStart.toLocalDateTime(TimeZone.UTC).date
                val lastMatchStart = playDay.value.last().startDate!!
                val dayDuration = gameDayDuration(firstMatchStart, lastMatchStart)
                val matchCount = playDay.value.count()
                DayInfo(
                    date = firstMatchDate,
                    count = matchCount,
                    duration = dayDuration,
                    firstMatchStart = firstMatchStart
                )
            }

            val rateRange = MAX_UPDATE_RATE_IN_SECONDS..MIN_UPDATE_RATE_IN_SECONDS
            val distribution = LimitsDistributor(monthAndMatches, rateRange).distribute(remainedLimit)

            val matchdaysDistribution = distribution.mapValues {
                val dayInfo = monthAndMatches.getValue(it.key)
                val updateRate = convertToMinutes(it.value.rateInSeconds)
                DayLimit(
                    gameDayDuration = dayInfo.duration,
                    plannedDayLimit = it.value.plannedRequestCount,
                    firstMatchStartOrDefault = dayInfo.firstMatchStart,
                    remainedDayLimit = it.value.plannedRequestCount,
                    updateRate = updateRate
                )
            }.mapKeys { it.value.firstMatchStartOrDefault.toLocalDateTime(TimeZone.UTC).date }

            val nonMatchdayDistribution = getNonMatchdayDayRates(
                matchdaysDistribution,
                Month(month),
                year,
                leapYear
            )

            dayLimitMap.putAll(matchdaysDistribution + nonMatchdayDistribution)
        }

        return if (!hasMatchesThisMonth) {
            // bc otherwise no updates for this month
            dayLimitMap.fillCurrentMonthAsNonMatchdays(today)
        } else {
            dayLimitMap
        }
    }

    private fun getNonMatchdayDayRates(
        limitsWithMatches: Map<LocalDate, *>,
        month: Month,
        year: Int,
        leapYear: Boolean
    ): Map<LocalDate, DayLimit> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val monthDays = if (today.month == month) {
            (today.dayOfMonth..month.length(leapYear))
        } else {
            (1..month.length(leapYear))
        }.toMutableList()
            .apply {
                limitsWithMatches.filter { it.key.month == month }
                    .onEach { this.remove(it.key.dayOfMonth) }
            }

        return buildMap {
            for (monthDay in monthDays) {
                val date = LocalDate(year, month, monthDay)
                put(
                    date,
                    DayLimit(
                        gameDayDuration = Duration.ZERO,
                        firstMatchStartOrDefault = date.atTime(
                            SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS,
                            SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES
                        ).toInstant(TimeZone.UTC),
                        plannedDayLimit = 1,
                        remainedDayLimit = 1,
                        updateRate = safeDayToDayUpdateRate.inWholeMinutes * 1.0
                    )
                )
            }
        }
    }

    private fun MutableDayLimitMap.fillCurrentMonthAsNonMatchdays(today: LocalDate): DayLimitMap {
        val nextDay = today.plus(1, DateTimeUnit.DAY)
        if (nextDay.month == today.month) {
            val isLeapYear = today.year.isLeapYear
            val monthLength = today.month.length(isLeapYear)

            for (day in nextDay.dayOfMonth..monthLength) {
                val date = LocalDate(today.year, today.month, day)
                this[date] = DayLimit(
                    gameDayDuration = Duration.ZERO,
                    firstMatchStartOrDefault = date.atTime(
                        SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS,
                        SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES
                    ).toInstant(UtcOffset.ZERO),
                    plannedDayLimit = 1,
                    remainedDayLimit = 1,
                    updateRate = convertToMinutes(safeDayToDayUpdateRate.inWholeSeconds.toInt())
                )
            }
        }

        return this
    }

    private fun convertToMinutes(rateInSeconds: Int): Double {
        return String.format("%.2f", rateInSeconds / 60.0).toDouble()
    }

    private fun gameDayDuration(
        firstMatchDate: Instant,
        lastMatchDate: Instant
    ) = if (firstMatchDate == lastMatchDate) {
        MATCH_DURATION_IN_MINUTES.minutes
    } else {
        lastMatchDate.minus(firstMatchDate)
            .plus(MATCH_DURATION_IN_MINUTES.minutes)
    }

    private fun countNotMatchDays(
        monthAndDays: Map.Entry<MonthAndYear, Map<Int, List<MatchdayDTO>>>,
        today: LocalDate,
        leapYear: Boolean
    ): Int {
        val (month, year) = monthAndDays.key
        val monthLength = Month(month).length(leapYear)

        return if (today.monthNumber == month && today.year == year) {
            1 + monthLength - (monthAndDays.value.keys.count() + today.dayOfMonth)
        } else {
            monthLength - monthAndDays.value.keys.count()
        }
    }

    companion object {
        private const val MAX_UPDATE_RATE_IN_SECONDS = 150 // 2.5 min
        private const val MIN_UPDATE_RATE_IN_SECONDS = 1800 // 30 min
        private const val MATCH_DURATION_IN_MINUTES = 140

        private val safeDayToDayUpdateRate: Duration = SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS.hours
    }
}