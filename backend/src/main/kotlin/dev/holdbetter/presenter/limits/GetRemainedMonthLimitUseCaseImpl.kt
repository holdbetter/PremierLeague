package dev.holdbetter.presenter.limits

import dev.holdbetter.interactor.DatabaseGateway

internal class GetRemainedMonthLimitUseCaseImpl(
    private val databaseGateway: DatabaseGateway
) : GetRemainedMonthLimitUseCase {
    override suspend fun getRemainedMonthLimit(
        month: Int,
        year: Int
    ) = databaseGateway.getRemainedMonthLimit(month, year)
}