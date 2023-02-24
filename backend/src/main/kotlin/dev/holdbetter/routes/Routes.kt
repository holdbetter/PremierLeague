package dev.holdbetter.routes

import dev.holdbetter.routes.PremierLeagueBackendEndpoints.STANDINGS
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
    val api: StandingsApi by kodein.instance()
    val country: Country by kodein.instance()
    val league: League by kodein.instance()

    val livescoreData = api.getLeague(league, country)

    if (livescoreData != null) {
        call.respond(livescoreData)
    } else {
        call.respond(HttpStatusCode.NotFound, "Can't be deserialized")
    }
}