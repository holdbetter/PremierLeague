package dev.holdbetter.coreMvi

import kotlinx.coroutines.flow.Flow

typealias Actor<State, Intent, Effect> = suspend (State, Intent) -> Flow<Effect>
typealias Reducer<State, Effect> = (State, Effect) -> State

interface Consumer<in T> {
    suspend fun accept(next: T)
}