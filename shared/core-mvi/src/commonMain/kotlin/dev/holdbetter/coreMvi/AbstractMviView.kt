package dev.holdbetter.coreMvi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class AbstractMviView<in Model, Event> : MviView<Model, Event> {

    private val stateFlow = MutableSharedFlow<Event>()

    override val events: Flow<Event>
        get() = stateFlow

    protected suspend fun dispatch(event: Event) {
        stateFlow.emit(event)
    }
}