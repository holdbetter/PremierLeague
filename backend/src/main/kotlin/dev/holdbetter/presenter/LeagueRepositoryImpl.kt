package dev.holdbetter.presenter

import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.interactor.LeagueDataSource
import dev.holdbetter.interactor.LeagueRepository
import kotlinx.datetime.*
import kotlin.time.Duration

internal class LeagueRepositoryImpl(
    override val dataSource: LeagueDataSource
) : LeagueRepository {

    override suspend fun updateLeagueData(league: League, country: Country) =
        dataSource.updateLeagueData(league, country)

    override suspend fun tryDecreaseLimit(): Boolean {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        return dataSource.database.tryDecreaseRemainedLimit(today)
    }

    override suspend fun hasLimits() = dataSource.database.hasLimits()

    override suspend fun getDelayDuration(): Duration {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val nextDay = today.date.plus(1, DateTimeUnit.DAY)
        return dataSource.calculateDelay(
            today = today,
            todayLimit = dataSource.database.getLimitsForDate(today.date),
            nextDayLimit = dataSource.database.getLimitsForDate(nextDay)
        )
    }
}