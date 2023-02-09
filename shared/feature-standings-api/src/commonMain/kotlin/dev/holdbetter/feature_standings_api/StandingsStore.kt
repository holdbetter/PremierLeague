package dev.holdbetter.feature_standings_api

import dev.holdbetter.coreMvi.Store
import dev.holdbetter.feature_standings_api.StandingsStore.Intent
import dev.holdbetter.feature_standings_api.StandingsStore.State

interface StandingsStore : Store<Intent, State> {

    sealed interface Intent {
        object Reload : Intent
        data class OpenTeamDetail(val teamId: Long) : Intent
    }

    data class State(
        val isLoading: Boolean = false,
        val data: Data? = null,
        val selectedTeam: Data.Standings.TeamRank? = null
    ) {

        sealed interface Data {
            data class Standings(
                val name: String,
                val country: String,
                val logoUrl: String,
                val teams: List<TeamRank>,
                val update: String
            ) : Data {

                data class TeamRank(
                    val rank: Int,
                    val points: Int,
                    val team: Team,
                    val allStats: Stats,
                    val homeStats: Stats,
                    val awayStats: Stats
                )

                data class Team(
                    val id: Long,
                    val name: String,
                    val logoUrl: String
                )

                data class Stats(
                    val played: Int,
                    val win: Int,
                    val draw: Int,
                    val lose: Int,
                    val goals: Goals
                )

                data class Goals(
                    val goalsFor: Long,
                    val against: Long
                )
            }

            data class Error(val throwable: Throwable) : Data
        }
    }
}