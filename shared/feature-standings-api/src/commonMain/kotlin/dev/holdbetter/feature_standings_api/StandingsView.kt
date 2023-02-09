package dev.holdbetter.feature_standings_api

import dev.holdbetter.coreMvi.MviView

interface StandingsView : MviView<StandingsView.Model, StandingsView.Event> {

    data class Model(
        val isLoading: Boolean,
        val isError: Boolean,
        val standings: StandingsStore.State.Data.Standings?
    )

    sealed interface Event {
        data class TeamSelected(val teamId: Long) : Event
        object Reload : Event
    }
}