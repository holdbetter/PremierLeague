package dev.holdbetter.shared.feature_team_detail

interface TeamDetailRepository {
    suspend fun getTeamDetail(teamId: Long): TeamDetailStore.State.Data.TeamDetail
    suspend fun changeTeamFavorite(teamId: Long): Boolean
    fun findNearMatch(matches: List<Match>): Match
}