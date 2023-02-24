package dev.holdbetter.common.util

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