package dev.holdbetter.core_network

import dev.holdbetter.core_network.model.Parameter
import io.ktor.http.*

fun ParametersBuilder.add(parameter: Parameter) {
    append(parameter.name, parameter.value)
}

// TODO: Test
fun Parameter.asQueryParameter() = "$name=$value"