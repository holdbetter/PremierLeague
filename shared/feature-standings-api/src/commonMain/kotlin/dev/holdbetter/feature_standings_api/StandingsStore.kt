package dev.holdbetter.feature_standings_api

import dev.holdbetter.common.GameResult
import dev.holdbetter.common.MatchdayDTO
import dev.holdbetter.coreMvi.Store
import dev.holdbetter.feature_standings_api.StandingsStore.Intent
import dev.holdbetter.feature_standings_api.StandingsStore.State

interface StandingsStore : Store<Intent, State> {

    sealed interface Intent {
        object Reload : Intent
        data class OpenTeamDetail(val teamId: String) : Intent
        object NavigationCommit : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val isRefreshEnabled: Boolean = false,
        val data: Data? = null,
        val selectedTeam: Data.Standings.TeamRank? = null
    ) {

        sealed interface Data {
            data class Standings(
                val teams: List<TeamRank>
            ) : Data {

                data class TeamRank(
                    val id: String,
                    val rank: Int,
                    val name: String,
                    val image: String,
                    val gamePlayed: Int,
                    val points: Int,
                    val wins: Int,
                    val loses: Int,
                    val draws: Int,
                    val goalsFor: Int,
                    val goalsAgainst: Int,
                    val goalsDiff: Int,
                    val lastResults: List<GameResult>,
                    val liveMatch: MatchdayDTO?
                )
            }

            data class Error(val throwable: Throwable) : Data
        }
    }
}