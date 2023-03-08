package dev.holdbetter.routes

import dev.holdbetter.interactor.DatabaseGateway
import dev.holdbetter.routes.PremierLeagueBackendEndpoints.STANDINGS
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Routing.home() = get("/") {
    call.respondText("Hello World!")
}

fun Routing.standings() = get("/$STANDINGS") {
    val kodein = closestDI()
    val database: DatabaseGateway by kodein.instance()
    call.respond(database.getStandings())
}