package dev.holdbetter.shared.feature_team_detail_impl.data

import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.core_network.DataSource.Database
import dev.holdbetter.core_network.DataSource.Network
import dev.holdbetter.core_network.model.TeamId

internal interface TeamDetailDataSource : Network, Database {
    suspend fun getTeamWithMatches(teamId: TeamId): TeamWithMatchesDTO
    suspend fun isTeamFavorite(teamId: Long): Boolean
    suspend fun addFavoriteTeam(teamId: Long)
    suspend fun removeFavoriteTeam(teamId: Long)
}