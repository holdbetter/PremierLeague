package dev.holdbetter.routes

import dev.holdbetter.common.util.decodeWith
import dev.holdbetter.network.ApiFootballConfig
import dev.holdbetter.outerApi.util.LivescoreUnwrapper
import dev.holdbetter.util.add
import dev.holdbetter.util.responseAsJsonElement
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

internal class StandingsApiImpl(
    override val client: HttpClient,
    override val decoder: Json,
    private val livescoreUnwrapper: LivescoreUnwrapper
) : StandingsApi {
    override suspend fun getLeague(league: League, country: Country) =
        client.get {
            url {
                appendPathSegments(ApiFootballConfig.Paths.UNIVERSAL_DATA)
                with(parameters) {
                    add(country)
                    add(league)
                }
            }
        }.responseAsJsonElement()
            .decodeWith(decoder, livescoreUnwrapper)
}