package dev.holdbetter.feature_standings_impl.data

import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.util.decode
import dev.holdbetter.core_network.LeagueBackendService
import dev.holdbetter.core_network.NetworkInteractor
import kotlinx.serialization.json.Json

internal class StandingsDataSourceImpl(
    override val decoder: Json,
    override val networkInteractor: NetworkInteractor
) : StandingsDataSource {

    override val paths by lazy {
        arrayOf(
            LeagueBackendService.Paths.STANDINGS
        )
    }

    override suspend fun getStandings(): List<TeamRankDTO> =
        networkInteractor.get(paths).run(decoder::decode)
}