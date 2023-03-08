package dev.holdbetter.feature_standings_impl.data

import dev.holdbetter.common.LeagueDTO
import dev.holdbetter.core_network.DataSource.Network

internal interface StandingsDataSource : Network {
    suspend fun getStandings(): LeagueDTO
}