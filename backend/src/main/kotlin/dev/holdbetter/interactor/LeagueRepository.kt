package dev.holdbetter.interactor

import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import kotlin.time.Duration

internal interface LeagueRepository {

    val dataSource: LeagueDataSource

    /**
     * Updates league data (standings, match-days, limits)
     *
     * @return duration delay to the next data update
     */
    suspend fun updateLeagueData(league: League, country: Country): Duration
    suspend fun hasLimits(): Boolean
    suspend fun tryDecreaseLimit(): Boolean
    suspend fun getDelayDuration(): Duration
}