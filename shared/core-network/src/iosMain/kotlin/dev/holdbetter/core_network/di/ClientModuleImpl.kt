package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.DarwinHttpClientFactory
import dev.holdbetter.core_network.LeagueBackendEndpoints
import dev.holdbetter.core_network.model.Client
import kotlinx.serialization.json.Json

class ClientModuleImpl : ClientModule {

    override val decoder by lazy(::getJsonDecoder)
    override val client = Client(LeagueBackendEndpoints.CLIENT)
    override val httpClient by lazy(DarwinHttpClientFactory::createClient)

    private fun getJsonDecoder() = Json {
        ignoreUnknownKeys = true
    }
}