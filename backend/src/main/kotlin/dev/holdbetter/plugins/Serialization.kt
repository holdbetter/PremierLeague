package dev.holdbetter.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureSerialization() {
    val json: Json by closestDI().instance()

    install(ContentNegotiation) {
        json(json)
    }
}
