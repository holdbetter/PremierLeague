package dev.holdbetter.compose.feature_standings

import androidx.compose.runtime.*
import dev.holdbetter.coreMvi.Store
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

@Composable
fun <Event, Intent, State, Model> ComposeComponent(
    store: Store<Intent, State>,
    toModel: State.() -> Model,
    toIntent: Event.() -> Intent,
    content: @Composable (model: Model, dispatcher: suspend (Event) -> Unit) -> Unit
) {
    val uiEvents = remember { MutableSharedFlow<Event>() }
    val dispatchEvent = uiEvents::emit

    val state by store.collectAsState(store.state)
    val model = state.toModel()

    LaunchedEffect(store) {
        uiEvents.map(toIntent)
            .collect(store::accept)
    }

    Napier.d {
        state.toString()
    }

//    TODO: needs navigation-based scope to stop it's coroutine
//    DisposableEffect(store) {
//        onDispose { store.dispose() }
//    }

    content(model, dispatchEvent)
}