package dev.holdbetter.database.dao


internal interface MonthLimitDao : ExposedDao {
    suspend fun getRemainedLimit(month: Int, year: Int): Int
    suspend fun fillLimits(monthToYears: Map<Int, Int>)
    suspend fun decreaseLimit(month: Int, year: Int)
    suspend fun hasData(): Boolean
}