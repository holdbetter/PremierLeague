package dev.holdbetter.presenter.limits

interface GetRemainedMonthLimitUseCase {
    suspend fun getRemainedMonthLimit(month: Int, year: Int): Int
}