package dev.holdbetter.routes

import dev.holdbetter.premierleague.LeagueDTO
import dev.holdbetter.routes.ApiFootballConfig.CURRENT_SEASON
import dev.holdbetter.routes.ApiFootballServiceEndpoints.STANDINGS
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
    val currentSeason: Season by kodein.instance(arg = CURRENT_SEASON)
    val league: League by kodein.instance()

    val leagueDTO: LeagueDTO? = api.getLeague(currentSeason, league)

    if (leagueDTO != null) {
        call.respond(leagueDTO)
    } else {
        call.respond(HttpStatusCode.NotFound, "Can't be deserialized")
    }
}