package dev.holdbetter.feature_standings_impl.data

import dev.holdbetter.common.LeagueDTO
import dev.holdbetter.common.util.decode
import dev.holdbetter.core_network.ClientEndpoints
import dev.holdbetter.core_network.NetworkInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal class StandingsDataSourceImpl(
    override val decoder: Json,
    override val networkInteractor: NetworkInteractor
) : StandingsDataSource {

    override val paths by lazy {
        arrayOf(
            ClientEndpoints.Paths.STANDINGS
        )
    }

    override suspend fun getStandings(): Flow<LeagueDTO> {
        return networkInteractor.get(*paths)
            .map(decoder::decode)
    }
}