package dev.holdbetter.shared.feature_team_detail_impl.data

import dev.holdbetter.common.TeamWithMatchesDTO
import dev.holdbetter.core_network.DataSource.Network
import dev.holdbetter.core_network.model.TeamId

internal interface TeamDetailDataSource : Network {
    suspend fun getTeamWithMatches(teamId: TeamId): TeamWithMatchesDTO
}