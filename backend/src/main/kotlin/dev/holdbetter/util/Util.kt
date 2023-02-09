package dev.holdbetter.util

import dev.holdbetter.routes.Parameter
import io.ktor.http.*
import io.ktor.server.application.*

val Application.isLocalBuild
    get() = environment.developmentMode

fun ParametersBuilder.add(parameter: Parameter) {
    append(parameter.name, parameter.value)
}