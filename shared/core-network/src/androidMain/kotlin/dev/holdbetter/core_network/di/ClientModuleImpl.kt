package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.LeagueBackendService
import dev.holdbetter.core_network.OkHttpClientFactory
import dev.holdbetter.core_network.OkHttpInterceptor
import dev.holdbetter.core_network.model.Client
import dev.shustoff.dikt.CreateSingle
import kotlinx.serialization.json.Json

internal class ClientModuleImpl : ClientModule {

    override val client = Client(LeagueBackendService.CLIENT)

    override val decoder by lazy(::getJsonDecoder)

    override val httpClient by lazy {
        OkHttpClientFactory.createClient {
            addInterceptor(getInterceptor())
        }
    }

    @CreateSingle
    private fun getInterceptor(): OkHttpInterceptor

    private fun getJsonDecoder() = Json {
        ignoreUnknownKeys = true
    }
}