package dev.holdbetter.compose.feature_standings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import dev.holdbetter.feature_standings_api.StandingsStore
import dev.holdbetter.feature_standings_api.StandingsStore.State
import dev.holdbetter.feature_standings_api.StandingsView.Event
import dev.holdbetter.feature_standings_impl.domain.toIntent
import dev.holdbetter.feature_standings_impl.domain.toModel
import dev.holdbetter.shared.core_database.api.DatabaseApi
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
@Inject
fun StandingsRoute(store: StandingsStore, databaseApi: DatabaseApi) {
    val scope = rememberCoroutineScope()

    ComposeComponent(
        store = store,
        toModel = State::toModel,
        toIntent = Event::toIntent,
    ) { model, dispatcher ->

        // TODO: move to State
        val favoriteTeamIds by produceState(
            initialValue = emptySet(),
            key1 = model.standings?.teams,
        ) {
            value = withContext(Dispatchers.Default) {
                databaseApi.favoritesApi().getFavoriteTeamIds().toSet()
            }
        }

        StandingsScreen(model, favoriteTeamIds, dispatcher)
    }
}