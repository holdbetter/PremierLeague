package dev.holdbetter.database.dao

import dev.holdbetter.common.TeamRankDTO
import dev.holdbetter.common.TeamWithMatchesDTO

internal interface TeamDao : ExposedDao {

    suspend fun getTeams(): List<TeamRankDTO>
    suspend fun getTeamWithMatches(teamId: String): TeamWithMatchesDTO?

    suspend fun insertTeams(teams: List<TeamRankDTO>)
    suspend fun updateTeams(teams: List<TeamRankDTO>)

    suspend fun hasData(): Boolean
}