package dev.holdbetter.coreMvi

import io.github.aakira.napier.Napier
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.contracts.Effect

@OptIn(FlowPreview::class)
class StoreHelper<in Intent, in Effect, State>(
    initialState: State,
    private val actor: Actor<State, Intent, Effect>,
    private val reducer: Reducer<State, Effect>,
    private val scope: CoroutineScope
) : AbstractFlow<State>(), Store<Intent, State> {

    override val state: State
        get() = stateFlow.value

    private val stateFlow: MutableStateFlow<State> = MutableStateFlow(initialState)

    override suspend fun accept(intent: Intent) {
        actor(stateFlow.value, intent)
            .cancellable()
            .onEach(::onEffect)
            .launchIn(scope)
    }

    override suspend fun collectSafely(collector: FlowCollector<State>) = stateFlow.collect(collector)

    override fun dispose() {
        scope.coroutineContext.cancelChildren()
    }

    private suspend fun onEffect(effect: Effect) {
        // TODO: add debug flags
        Napier.d { "onEffect: $effect"}
        stateFlow.emit(reducer(stateFlow.value, effect))
    }
}