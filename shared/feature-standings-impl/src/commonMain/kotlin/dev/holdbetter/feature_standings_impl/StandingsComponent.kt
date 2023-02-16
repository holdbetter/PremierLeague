package dev.holdbetter.feature_standings_impl

import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_api.StandingsView
import dev.holdbetter.feature_standings_impl.domain.toIntent
import dev.holdbetter.feature_standings_impl.domain.toModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map

internal class StandingsComponent(private val store: StandingsStore) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var view: StandingsView? = null

    fun onViewCreated(view: StandingsView) {
        this.view = view
    }

    fun onStart() {
        view?.let { view ->
            scope.launch {
                store.map(StandingsStore.State::toModel)
                    .collect(view::render)
            }

            scope.launch {
                view.events
                    .map(StandingsView.Event::toIntent)
                    .collect(store::accept)
            }
        }
    }

    fun onStop() {
        scope.coroutineContext.cancelChildren()
    }

    fun onViewDestroyed() {
        view = null
    }

    fun onDestroy() {
        store.dispose()
    }
}