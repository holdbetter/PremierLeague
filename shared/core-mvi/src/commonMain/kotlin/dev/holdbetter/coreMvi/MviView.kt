package dev.holdbetter.coreMvi

import kotlinx.coroutines.flow.Flow

interface MviView<in Model, out Event> {
    val events: Flow<Event>

    fun render(model: Model)
}