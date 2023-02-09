package dev.holdbetter.routes

import dev.holdbetter.network.RapidResponse.decode
import dev.holdbetter.network.RapidResponse.response
import dev.holdbetter.premierleague.LeagueDTO
import dev.holdbetter.util.add
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class StandingsApiImpl(
    override val client: HttpClient,
    override val decoder: Json
) : StandingsApi {

    override suspend fun getLeague(season: Season, league: League): LeagueDTO? {
        return client.get {
            url {
                appendPathSegments(ApiFootballServiceEndpoints.STANDINGS)
                with(parameters) {
                    add(season)
                    add(league)
                }
            }
        }.response()
            .decode(decoder)
    }
}