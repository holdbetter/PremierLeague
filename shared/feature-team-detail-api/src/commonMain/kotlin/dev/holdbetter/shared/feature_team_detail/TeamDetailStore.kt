package dev.holdbetter.shared.feature_team_detail

import dev.holdbetter.coreMvi.Store
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore.Intent
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore.State
import kotlinx.datetime.Month

interface TeamDetailStore : Store<Intent, State> {

    sealed interface Intent {
        object Reload : Intent
        // TODO: Compare
        // TODO: AddToFavorites
        // TODO: LinkClicked
    }

    data class State(
        val teamId: Long,
        val isLoading: Boolean = false,
        val isRefreshEnabled: Boolean = false,
        val data: Data? = null
    ) {
        sealed interface Data {
            data class TeamDetail(
                val team: Team,
                val allMatches: List<Match>,
                val pastResultsByMonth: Map<Month, MonthResult>
            ) : Data

            data class Error(
                val throwable: Throwable
            ) : Data
        }
    }
}