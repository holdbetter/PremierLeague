package dev.holdbetter.feature_standings_api

import dev.holdbetter.coreMvi.MviView

interface StandingsView : MviView<StandingsView.Model, StandingsView.Event> {

    data class Model(
        val isLoading: Boolean,
        val isRefreshEnabled: Boolean,
        val isError: Boolean,
        val standings: StandingsStore.State.Data.Standings?,
        var selectedTeam: SelectedTeam?
    )

    sealed interface Event {
        data class TeamSelected(val teamId: String) : Event
        object Reload : Event
        object NavigationCommit : Event
    }
}