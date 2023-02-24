package dev.holdbetter.routes

import dev.holdbetter.outerApi.model.LivescoreDataResponse
import io.ktor.client.*
import kotlinx.serialization.json.Json

internal interface StandingsApi {

    val client: HttpClient
    val decoder: Json

    suspend fun getLeague(league: League, country: Country): LivescoreDataResponse?
}