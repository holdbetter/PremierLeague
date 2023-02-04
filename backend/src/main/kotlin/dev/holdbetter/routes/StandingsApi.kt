package dev.holdbetter.routes

import dev.holdbetter.common.LeagueDTO
import io.ktor.client.*
import kotlinx.serialization.json.Json

interface StandingsApi {

    val client: HttpClient
    val decoder: Json

    suspend fun getLeague(season: Season, league: League): LeagueDTO?
}