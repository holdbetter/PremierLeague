package dev.holdbetter.shared.feature_team_detail

import dev.holdbetter.coreMvi.MviView
import dev.holdbetter.shared.feature_team_detail.TeamDetailView.Event
import dev.holdbetter.shared.feature_team_detail.TeamDetailView.Model

interface TeamDetailView : MviView<Model, Event> {

    data class Model(
        val isLoading: Boolean,
        val isRefreshEnabled: Boolean,
        val isError: Boolean,
        val teamWithMatches: TeamDetailStore.State.Data.TeamDetail?
    )

    sealed interface Event {
        object Reload : Event
    }
}