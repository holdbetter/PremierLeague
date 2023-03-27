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

    const val SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS = 24
    const val SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS = 15
    const val SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES = 30

    private const val MONTH_LIMIT = 500
    private const val MAX_UPDATE_RATE = 3
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
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val isCurrentMonthPresent = groupedMatches.containsKey(today.monthNumber)
        return groupedMatches.forEach { monthAndMatchesByDay ->
            val nonMatchDayCount = countNotMatchDays(monthAndMatchesByDay, leapYear)
            when (index) {
                0 -> dayLimitMap.fillLimitsForMonth(
                    monthAndMatchesByDay.value,
                    nonMatchDayCount,
                    usedLimit
                )
                else -> dayLimitMap.fillLimitsForMonth(
                    monthAndMatchesByDay.value,
                    nonMatchDayCount,
                    0
                )
            }
            dayLimitMap.fullFillMonthWithNonMatchday(Month(monthAndMatchesByDay.key), leapYear)
            index++
        }.run { if (!isCurrentMonthPresent) dayLimitMap.fillCurrentMonth(today) else dayLimitMap }
    }

    private fun MutableDayLimitMap.fillCurrentMonth(today: LocalDate): DayLimitMap {
        val nextDay = today.plus(1, DateTimeUnit.DAY)
        if (nextDay.month == today.month) {
            val isLeapYear = today.year % 4 == 0
            val monthLength = today.month.length(isLeapYear)

            for (day in nextDay.dayOfMonth..monthLength) {
                val date = LocalDate(today.year, today.month, day)
                this[date] = Limit(
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
        usedLimit: Int
    ) {
        var restart = true
        restart@ while (restart) {
            var monthLimit = MONTH_LIMIT - SAFE_BUFFER - nonMatchDayCount - usedLimit
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
                monthLimit -= dayLimit

                this[day] = Limit(
                    gameDayDuration = gameDayDuration,
                    firstMatchStartOrDefault = firstMatchStart,
                    plannedDayLimit = dayLimit,
                    remainedDayLimit = dayLimit,
                    updateRate = rate
                )

                if (monthLimit.isExceeded) {
                    decreaseRate(day.monthNumber)
                    continue@restart
                }
            }
            restart = false
        }
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