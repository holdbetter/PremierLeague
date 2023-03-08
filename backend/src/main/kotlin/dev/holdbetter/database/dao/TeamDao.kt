package dev.holdbetter.database.dao

import dev.holdbetter.innerApi.model.TeamRank
import dev.holdbetter.innerApi.model.TeamWithMatches

internal interface TeamDao : ExposedDao {

    suspend fun getTeams(): List<TeamRank>
    suspend fun getTeamWithMatches(teamId: String): TeamWithMatches?

    suspend fun insertTeams(teams: List<TeamRank>)
    suspend fun updateTeams(teams: List<TeamRank>)

    suspend fun hasData(): Boolean
}