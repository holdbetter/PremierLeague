package dev.holdbetter.shared.feature_team_detail_impl

import dev.holdbetter.shared.feature_team_detail.TeamDetailStore
import dev.holdbetter.shared.feature_team_detail.TeamDetailView
import dev.holdbetter.shared.feature_team_detail_impl.domain.toIntent
import dev.holdbetter.shared.feature_team_detail_impl.domain.toModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.map

internal class TeamDetailComponent(private val store: TeamDetailStore) {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var view: TeamDetailView? = null

    fun onViewCreated(view: TeamDetailView) {
        this.view = view
    }

    fun onStart() {
        view?.let { view ->
            scope.launch {
                store.map(TeamDetailStore.State::toModel)
                    .collect(view::render)
            }

            scope.launch {
                view.events
                    .map(TeamDetailView.Event::toIntent)
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