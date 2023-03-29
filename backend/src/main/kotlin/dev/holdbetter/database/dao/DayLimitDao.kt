package dev.holdbetter.database.dao

import dev.holdbetter.innerApi.model.DayLimit
import dev.holdbetter.presenter.DayLimitMap
import kotlinx.datetime.LocalDate

internal interface DayLimitDao : ExposedDao {
    suspend fun insertDayLimits(dayLimitMap: DayLimitMap)
    suspend fun updateDayLimits(dayLimitMap: DayLimitMap)
    suspend fun decreaseLimitByDate(date: LocalDate)
    suspend fun getLimits(): DayLimitMap
    suspend fun getLimitsForDate(date: LocalDate): DayLimit
    suspend fun hasData(): Boolean
}