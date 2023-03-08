package dev.holdbetter.presenter

import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.innerApi.model.Limit
import kotlinx.datetime.*
import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

typealias DayLimitMap = Map<LocalDate, Limit>
private typealias MutableDayLimitMap = LinkedHashMap<LocalDate, Limit>
private typealias MonthGroup = Map<Int, List<MatchdayDTO>>
private typealias MonthLimit = Int

// TODO: Test
internal object LimitsResolver {

    private const val MONTH_LIMIT = 500
    private const val MAX_UPDATE_RATE = 3
    private const val SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS = 24
    private const val MATCH_DURATION_IN_MINUTES = 140
    private const val SAFE_BUFFER = 40

    private val safeDayToDayUpdateRate: Duration = SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS.hours

    private val MonthLimit.isExceeded: Boolean
        get() = this < 0

    // TODO: forEach can be parallel with synchronizedMap or list of maps appending at the end
    // TODO: also index should by sync
    fun generateDayLimits(
        groupedMatches: MonthAndDayGroupedMatchesDaySorted,
        leapYear: Boolean,
        usedLimit: Int = 0
    ): DayLimitMap {
        val dayLimitMap: MutableDayLimitMap = linkedMapOf()
        var index = 0
        return groupedMatches.forEach {
            val nonMatchDayCount = countNotMatchDays(it, leapYear)
            when (index) {
                0 -> dayLimitMap.fillLimitsForMonth(it.value, nonMatchDayCount, usedLimit)
                else -> dayLimitMap.fillLimitsForMonth(it.value, nonMatchDayCount, 0)
            }
            dayLimitMap.fullFillWithNonMatchday(Month(it.key), leapYear)
            index++
        }.run { dayLimitMap }
    }

    private fun MutableDayLimitMap.fillLimitsForMonth(
        monthGroup: MonthGroup,
        nonMatchDayCount: Int,
        usedLimit: Int
    ) {
        var restart = true
        restart@ while (restart) {
            var monthLimit = MONTH_LIMIT - SAFE_BUFFER - nonMatchDayCount - usedLimit
            for (playDay in monthGroup) {
                val firstMatchDate = playDay.value.first().startDate!!
                val lastMatchDate = playDay.value.last().startDate!!
                val today = firstMatchDate.date

                val (gameDayDuration, rate) = getOrComputeDurationWithRate(
                    today,
                    firstMatchDate,
                    lastMatchDate
                )

                val dayLimit = recalculateDayLimit(gameDayDuration, rate)
                monthLimit -= dayLimit

                this[today] = Limit(
                    gameDayDuration = gameDayDuration,
                    firstMatchStartOrDefault = firstMatchDate,
                    plannedDayLimit = dayLimit,
                    remainedDayLimit = dayLimit,
                    updateRate = rate
                )

                if (monthLimit.isExceeded) {
                    decreaseRate(today.monthNumber)
                    continue@restart
                }
            }
            restart = false
        }
    }

    private fun MutableDayLimitMap.getOrComputeDurationWithRate(
        today: LocalDate,
        firstMatchDate: LocalDateTime,
        lastMatchDate: LocalDateTime
    ) = this[today]?.run { gameDayDuration to updateRate }
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

    private fun MutableDayLimitMap.fullFillWithNonMatchday(
        month: Month,
        leapYear: Boolean
    ) {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val monthDays = if (today.month == month) {
            (today.dayOfMonth..month.length(leapYear)).toMutableList()
        } else {
            (1..month.length(leapYear)).toMutableList()
        }

        val currentYear = this.filter { it.key.month == month }
            .onEach { monthDays.remove(it.key.dayOfMonth) }
            .run { keys.first().year }

        for (monthDay in monthDays) {
            val date = LocalDate(currentYear, month, monthDay)
            this[date] = Limit(
                gameDayDuration = Duration.ZERO,
                firstMatchStartOrDefault = date.atTime(15, 30),
                plannedDayLimit = 1,
                remainedDayLimit = 1,
                updateRate = safeDayToDayUpdateRate.inWholeMinutes.toInt()
            )
        }
    }

    private fun gameDayDuration(
        firstMatchDate: LocalDateTime,
        lastMatchDate: LocalDateTime
    ) = if (firstMatchDate == lastMatchDate) {
        MATCH_DURATION_IN_MINUTES.minutes
    } else {
        lastMatchDate.toInstant(TimeZone.UTC)
            .minus(firstMatchDate.toInstant(TimeZone.UTC))
            .plus(MATCH_DURATION_IN_MINUTES.minutes)
    }

    private fun recalculateDayLimit(gameDayDuration: Duration, rate: Int): Int {
        val dayLimitF = gameDayDuration.inWholeMinutes.toInt() / rate.toFloat()
        return ceil(dayLimitF).toInt()
    }

    private fun decreaseRate(maxRateDateEntry: Map.Entry<LocalDate, Limit>): Limit {
        val currentRate = maxRateDateEntry.value.updateRate
        return maxRateDateEntry.value.copy(
            updateRate = currentRate + 1
        )
    }

    private fun countNotMatchDays(
        monthAndDays: Map.Entry<Int, Map<Int, List<MatchdayDTO>>>,
        leapYear: Boolean
    ): Int {
        val monthLength = Month(monthAndDays.key).length(leapYear)
        return monthLength - monthAndDays.value.keys.count()
    }
}