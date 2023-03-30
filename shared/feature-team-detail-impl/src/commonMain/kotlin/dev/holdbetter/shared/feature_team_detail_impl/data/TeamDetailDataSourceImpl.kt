package dev.holdbetter.shared.feature_team_detail_impl.data

import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.common.util.decode
import dev.holdbetter.core_network.LeagueBackendService
import dev.holdbetter.core_network.NetworkInteractor
import dev.holdbetter.core_network.model.TeamId
import dev.holdbetter.shared.core_database.api.DatabaseApi
import kotlinx.serialization.json.Json

internal class TeamDetailDataSourceImpl(
    override val decoder: Json,
    override val networkInteractor: NetworkInteractor,
    private val databaseApi: DatabaseApi
) : TeamDetailDataSource {

    override val paths: Array<String> = arrayOf(
        LeagueBackendService.Paths.TEAM
    )

    override suspend fun getTeamWithMatches(teamId: TeamId): TeamWithMatchesDTO {
        return networkInteractor.get(paths, teamId)
            .run(decoder::decode)
    }

    override suspend fun isTeamFavorite(teamId: Long): Boolean {
        return databaseApi.favoritesApi()
            .getFavoriteTeamIds()
            .contains(teamId)
    }

    override suspend fun addFavoriteTeam(teamId: Long) {
        databaseApi.favoritesApi().addFavorite(teamId)
    }

    override suspend fun removeFavoriteTeam(teamId: Long) {
        databaseApi.favoritesApi().removeFavorite(teamId)
    }
}