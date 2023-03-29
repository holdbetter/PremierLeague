package dev.holdbetter.presenter

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.interactor.DatabaseGateway
import dev.holdbetter.isLeapYear
import kotlinx.datetime.*
import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

typealias DayLimitMap = Map<LocalDate, DayLimit>
private typealias MutableDayLimitMap = LinkedHashMap<LocalDate, DayLimit>
private typealias MonthGroup = Map<Int, List<MatchdayDTO>>
private typealias MonthLimit = Int

// TODO: Test
internal object LimitsResolver {

    const val SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS = 24
    const val SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS = 15
    const val SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES = 30

    private const val MAX_UPDATE_RATE = 3
    private const val MATCH_DURATION_IN_MINUTES = 140

    private val safeDayToDayUpdateRate: Duration = SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS.hours

    private val MonthLimit.isExceeded: Boolean
        get() = this < 0

    // TODO: forEach can be parallel with synchronizedMap or list of maps appending at the end
    // TODO: also index should by sync
    /**
     * Generates map of limits with data update rate for matchdays and every day usage.
     *
     * Generally logic is simple:
     *  - fill limits for every month in group with matches
     *  - fill limits for every void day in month
     *  - optional: fill limits for current month if it doesn't present in group
     */
    suspend fun generateDayLimits(
        groupedMatches: SortedMatchesGroupedByDayAndSeasonalMonth,
        databaseGateway: DatabaseGateway,
        leapYear: Boolean
    ): DayLimitMap {
        val dayLimitMap: MutableDayLimitMap = linkedMapOf()
        var index = 0
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val isCurrentMonthPresent = groupedMatches.containsKey(today.monthNumber to today.year)
        return groupedMatches.forEach { monthAndMatchesByDay ->
            val (month, year) = monthAndMatchesByDay.key
            val nonMatchDayCount = countNotMatchDays(monthAndMatchesByDay, today, leapYear)
            val remainedLimit = databaseGateway.getRemainedMonthLimit(month, year)

            dayLimitMap.fillLimitsForMonth(
                monthGroup = monthAndMatchesByDay.value,
                nonMatchDayCount = nonMatchDayCount,
                monthLimit = remainedLimit
            ).run { fullFillMonthWithNonMatchday(Month(month), year, leapYear) }

            index += 1
        }.run { if (!isCurrentMonthPresent) dayLimitMap.fillCurrentMonth(today) else dayLimitMap }
    }

    // Not working with planned limits since it has minimal limits and 0 matches
    private fun MutableDayLimitMap.fillCurrentMonth(today: LocalDate): DayLimitMap {
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
                    updateRate = safeDayToDayUpdateRate.inWholeMinutes.toInt()
                )
            }
        }

        return this
    }

    private fun MutableDayLimitMap.fillLimitsForMonth(
        monthGroup: MonthGroup,
        nonMatchDayCount: Int,
        monthLimit: Int
    ): MutableDayLimitMap {
        var restart = true
        restart@ while (restart) {
            var resultMonthLimit = monthLimit - nonMatchDayCount
            for (playDay in monthGroup) {
                val firstMatchStart = playDay.value.first().startDate!!
                val lastMatchStart = playDay.value.last().startDate!!

                val day = firstMatchStart.toLocalDateTime(TimeZone.UTC).date

                val (gameDayDuration, rate) = getOrComputeDurationWithRate(
                    day,
                    firstMatchStart,
                    lastMatchStart
                )

                val dayLimit = recalculateDayLimit(gameDayDuration, rate)
                resultMonthLimit -= dayLimit

                this[day] = DayLimit(
                    gameDayDuration = gameDayDuration,
                    firstMatchStartOrDefault = firstMatchStart,
                    plannedDayLimit = dayLimit,
                    remainedDayLimit = dayLimit,
                    updateRate = rate
                )

                if (resultMonthLimit.isExceeded) {
                    decreaseRate(day.monthNumber)
                    continue@restart
                }
            }
            restart = false
        }

        return this
    }

    private fun MutableDayLimitMap.getOrComputeDurationWithRate(
        day: LocalDate,
        firstMatchDate: Instant,
        lastMatchDate: Instant
    ) = this[day]?.run { gameDayDuration to updateRate }
        ?: (gameDayDuration(firstMatchDate, lastMatchDate) to MAX_UPDATE_RATE)

    private fun MutableDayLimitMap.decreaseRate(monthNumber: Int) {
        val maxRateDateEntry = findMaxRateEntryInCurrentMonth(monthNumber)
        this[maxRateDateEntry.key] = decreaseRate(maxRateDateEntry)
    }

    private fun MutableDayLimitMap.findMaxRateEntryInCurrentMonth(monthNumber: Int) =
        this.asSequence()
            .filter { it.key.monthNumber == monthNumber }
            .filter { it.value.gameDayDuration.inWholeMinutes > it.value.updateRate }
            .minByOrNull { it.value.updateRate }
            .run(::requireNotNull)

    private fun MutableDayLimitMap.fullFillMonthWithNonMatchday(
        month: Month,
        year: Int,
        leapYear: Boolean
    ) {
        val mutableDayLimitMap = this
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val monthDays = if (today.month == month) {
            (today.dayOfMonth..month.length(leapYear))
        } else {
            (1..month.length(leapYear))
        }.toMutableList()
            .run {
                mutableDayLimitMap.filter { it.key.month == month }
                    .onEach { remove(it.key.dayOfMonth) }
                this
            }

        for (monthDay in monthDays) {
            val date = LocalDate(year, month, monthDay)
            this[date] = DayLimit(
                gameDayDuration = Duration.ZERO,
                firstMatchStartOrDefault = date.atTime(
                    SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS,
                    SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES
                ).toInstant(TimeZone.UTC),
                plannedDayLimit = 1,
                remainedDayLimit = 1,
                updateRate = safeDayToDayUpdateRate.inWholeMinutes.toInt()
            )
        }
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

    private fun recalculateDayLimit(gameDayDuration: Duration, rate: Int): Int {
        val dayLimitF = gameDayDuration.inWholeMinutes.toInt() / rate.toFloat()
        return ceil(dayLimitF).toInt()
    }

    private fun decreaseRate(maxRateDateEntry: Map.Entry<LocalDate, DayLimit>): DayLimit {
        val currentRate = maxRateDateEntry.value.updateRate
        return maxRateDateEntry.value.copy(
            updateRate = currentRate + 1
        )
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
}