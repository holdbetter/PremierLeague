package dev.holdbetter.feature_standings_impl.data

import dev.holdbetter.common.LeagueDTO
import dev.holdbetter.core_network.DataSource.Network
import kotlinx.coroutines.flow.Flow

internal interface StandingsDataSource : Network {

    val paths: Array<String>
        get() = emptyArray()

    suspend fun getStandings() : Flow<LeagueDTO>
}