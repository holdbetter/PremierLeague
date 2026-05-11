package dev.holdbetter.interactor

import dev.holdbetter.presenter.MatchesGroupedByDayAndSeasonalMonth
import dev.holdbetter.presenter.limits.DayLimitMap
import dev.holdbetter.presenter.limits.GetRemainedMonthLimitUseCase

interface DayLimitsGenerator {
    suspend fun generateDayLimits(
        groupedMatches: MatchesGroupedByDayAndSeasonalMonth,
        limitsStore: GetRemainedMonthLimitUseCase,
        leapYear: Boolean
    ): DayLimitMap

    object Config {
        const val SAFE_DAY_TO_DAY_UPDATE_RATE_IN_HOURS = 24
        const val SAFE_DAY_TO_DAY_UPDATE_TIME_HOURS = 15
        const val SAFE_DAY_TO_DAY_UPDATE_TIME_MINUTES = 30
    }
}