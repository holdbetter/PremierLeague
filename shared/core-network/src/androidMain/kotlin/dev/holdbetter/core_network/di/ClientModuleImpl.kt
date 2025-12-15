package dev.holdbetter.core_network.di

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.LeagueBackendService
import dev.holdbetter.core_network.OkHttpClientFactory
import dev.holdbetter.core_network.OkHttpInterceptor
import dev.holdbetter.core_network.model.Client
import dev.shustoff.dikt.ModuleScopes
import dev.shustoff.dikt.resolve
import kotlinx.serialization.json.Json

@ModuleScopes(Dikt::class)
internal class ClientModuleImpl : ClientModule {

    override val client = Client(LeagueBackendService.CLIENT)

    override val decoder by lazy(::getJsonDecoder)

    override val httpClient by lazy {
        OkHttpClientFactory.createClient {
            addInterceptor(getInterceptor())
        }
    }

    private fun getInterceptor(): OkHttpInterceptor = resolve()

    private fun getJsonDecoder() = Json {
        ignoreUnknownKeys = true
    }
}