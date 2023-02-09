package dev.holdbetter.coreMvi

import kotlinx.coroutines.flow.Flow

interface Store<in Intent, out State> : Consumer<Intent>, Flow<State>, Disposable {

    val state: State

    override suspend fun accept(intent: Intent)
}