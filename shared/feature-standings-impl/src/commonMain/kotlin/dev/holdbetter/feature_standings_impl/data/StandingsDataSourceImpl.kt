package dev.holdbetter.feature_standings_impl.data

import dev.holdbetter.common.LeagueDTO
import dev.holdbetter.common.util.decode
import dev.holdbetter.core_network.LeagueBackendEndpoints
import dev.holdbetter.core_network.NetworkInteractor
import kotlinx.serialization.json.Json

internal class StandingsDataSourceImpl(
    override val decoder: Json,
    override val networkInteractor: NetworkInteractor
) : StandingsDataSource {

    override val paths by lazy {
        arrayOf(
            LeagueBackendEndpoints.Paths.STANDINGS
        )
    }

    override suspend fun getStandings(): LeagueDTO =
        networkInteractor.get(paths).run(decoder::decode)
}