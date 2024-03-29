package dev.holdbetter.shared.feature_team_detail

import dev.holdbetter.coreMvi.MviView
import dev.holdbetter.shared.feature_team_detail.TeamDetailView.Event
import dev.holdbetter.shared.feature_team_detail.TeamDetailView.Model
import kotlinx.datetime.LocalDate

interface TeamDetailView : MviView<Model, Event> {

    data class Model(
        val isLoading: Boolean,
        val isRefreshing: Boolean,
        val isRefreshEnabled: Boolean,
        val isError: Boolean,
        val teamWithMatches: TeamDetailStore.State.Data.TeamDetail?,
        val twitterRedirect: Boolean
    )

    sealed interface Event {
        object Startup : Event
        object Refresh : Event
        object TwitterButtonClicked : Event
        object FavoritesClicked : Event
        object NavigationCommit : Event
        data class DateClicked(val date: LocalDate) : Event
    }
}