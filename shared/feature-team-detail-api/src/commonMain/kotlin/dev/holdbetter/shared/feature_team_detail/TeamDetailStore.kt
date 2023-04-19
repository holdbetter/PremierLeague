package dev.holdbetter.shared.feature_team_detail

import dev.holdbetter.coreMvi.Store
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore.Intent
import dev.holdbetter.shared.feature_team_detail.TeamDetailStore.State
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

interface TeamDetailStore : Store<Intent, State> {

    sealed interface Intent {
        // TODO: Compare
        data class MatchCardUpdate(val date: LocalDate) : Intent
        object ToggleFavorite : Intent
        object Startup : Intent
        object Refresh : Intent
        object RunTwitterRedirect : Intent
        object NavigationCommit : Intent
    }

    data class State(
        val teamId: Long,
        val isLoading: Boolean = false,
        val isRefreshing: Boolean = false,
        val isRefreshEnabled: Boolean = false,
        val data: Data? = null,
        val twitterRedirect: Boolean = false
    ) {
        sealed interface Data {
            data class TeamDetail(
                val team: Team,
                val allMatches: List<Match>,
                val pastResultsByMonth: Map<Month, MonthResult>,
                val isTeamFavorite: Boolean,
                val matchCard: Match,
                val nextMatch: Match,
                val calendar: List<DateHolder>,
                val isCompareAvailable: Boolean
            ) : Data

            data class Error(
                val throwable: Throwable
            ) : Data
        }
    }
}