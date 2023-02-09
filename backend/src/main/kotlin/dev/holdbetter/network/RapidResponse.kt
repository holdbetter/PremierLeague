package dev.holdbetter.network

import io.ktor.client.statement.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

// TODO: Надо написать тесты
object RapidResponse {
    private const val GET = "get"
    private const val RESPONSE = "response"
    private const val RESULTS = "results"
    private const val PARAMETERS = "parameters"
    private const val ERRORS = "errors"

    suspend fun HttpResponse.response() =
        Json.parseToJsonElement(bodyAsText())
            .jsonObject[RESPONSE]
            ?.jsonArray
            ?.singleOrNull()
            ?.jsonObject
            ?.entries
            ?.first()
            ?.value

    suspend fun HttpResponse.results() =
        Json.parseToJsonElement(bodyAsText())
            .jsonObject[RESULTS]
            ?.jsonPrimitive

    suspend fun HttpResponse.get() =
        Json.parseToJsonElement(bodyAsText())
            .jsonObject[GET]
            ?.jsonPrimitive

    suspend fun HttpResponse.parameters() =
        Json.parseToJsonElement(bodyAsText())
            .jsonObject[PARAMETERS]
            ?.jsonObject
            ?.mapValues { it.value.jsonPrimitive.toString() }

    suspend fun HttpResponse.errors() =
        Json.parseToJsonElement(bodyAsText())
            .jsonObject[ERRORS]
            ?.jsonObject
            ?.mapValues { it.value.jsonPrimitive.toString() }
}