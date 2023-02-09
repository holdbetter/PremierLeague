package dev.holdbetter.plugins

import dev.holdbetter.routes.home
import dev.holdbetter.routes.standings
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        home()
        standings()
    }
}