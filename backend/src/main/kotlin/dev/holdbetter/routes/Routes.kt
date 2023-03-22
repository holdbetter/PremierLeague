package dev.holdbetter.routes

import dev.holdbetter.core_network.LeagueBackendService.Paths.STANDINGS
import dev.holdbetter.core_network.LeagueBackendService.Paths.TEAM
import dev.holdbetter.core_network.model.TeamId
import dev.holdbetter.interactor.DatabaseGateway
import io.ktor.http.*
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

fun Routing.team() = get("/$TEAM") {
    val kodein = closestDI()
    val database: DatabaseGateway by kodein.instance()

    val teamIdParameter = call.request.queryParameters[TeamId.name]
    val teamId = teamIdParameter?.toLongOrNull()

    if (teamId != null) {
        val teamWithMatches = database.getTeamWithMatches(teamId.toString())
        if (teamWithMatches == null) {
            call.respond(HttpStatusCode.NotFound)
        } else {
            call.respond(teamWithMatches)
        }
    } else {
        call.respond(HttpStatusCode.BadRequest)
    }
}