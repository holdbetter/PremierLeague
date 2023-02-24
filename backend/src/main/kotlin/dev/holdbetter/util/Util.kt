package dev.holdbetter.util

import dev.holdbetter.routes.Parameter
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json

val Application.isLocalBuild
    get() = environment.developmentMode

fun ParametersBuilder.add(parameter: Parameter) {
    append(parameter.name, parameter.value)
}

fun Parameter.asQueryParameter() = "$name=$value"

suspend fun HttpResponse.responseAsJsonElement() = Json.parseToJsonElement(bodyAsText())