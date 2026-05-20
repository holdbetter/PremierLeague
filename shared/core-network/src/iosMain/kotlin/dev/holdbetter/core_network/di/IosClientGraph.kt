package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.DarwinHttpClientFactory
import dev.holdbetter.core_network.LeagueBackendService
import dev.holdbetter.core_network.model.Client
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

@DependencyGraph(AppScope::class)
abstract class IosClientGraph : ClientModule {

    @Provides
    fun provideClient(): Client = Client(LeagueBackendService.CLIENT)

    @SingleIn(AppScope::class)
    @Provides
    fun provideDecoder(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    private fun provideHttpClient(): HttpClient {
        return DarwinHttpClientFactory.createClient()
    }
}