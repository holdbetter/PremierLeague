package dev.holdbetter.common.util

import dev.holdbetter.common.Status
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

inline fun <reified T> JsonElement?.decode(json: Json): T? =
    this?.let { json.decodeFromJsonElement(it) }

inline fun <reified T> JsonElement?.decodeWith(
    json: Json,
    strategy: DeserializationStrategy<T>
): T? = this?.let { json.decodeFromJsonElement(strategy, it) }

inline fun <reified T> Json.decode(jsonString: String): T = this.decodeFromString(jsonString)
inline fun <reified T> Json.decodeWith(
    jsonString: String,
    strategy: DeserializationStrategy<T>
): T = this.decodeFromString(strategy, jsonString)

val Int.isRunning
    get() = when (this) {
        Status.NOT_STARTED.id, Status.POSTPONED.id, Status.FULL_TIME.id -> false
        Status.FIRST_TIME.id, Status.SECOND_TIME.id, Status.HALF_TIME.id -> true
        else -> false
    }

val Int.isGameOver
    get() = when (this) {
        Status.FULL_TIME.id -> true
        else -> false
    }