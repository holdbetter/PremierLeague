package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.LeagueBackendService
import dev.holdbetter.core_network.OkHttpClientFactory
import dev.holdbetter.core_network.OkHttpInterceptor
import dev.holdbetter.core_network.model.Client
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import okhttp3.Interceptor

@DependencyGraph(AppScope::class)
abstract class AndroidClientGraph : ClientModule {

    @Binds
    internal abstract val OkHttpInterceptor.bind: Interceptor

    @Provides
    fun provideClient(): Client = Client(LeagueBackendService.CLIENT)

    @SingleIn(AppScope::class)
    @Provides
    fun provideDecoder(): Json = Json {
        ignoreUnknownKeys = true
    }

    @SingleIn(AppScope::class)
    @Provides
    private fun provideHttpClient(okHttpInterceptor: Interceptor): HttpClient {
        return OkHttpClientFactory.createClient {
            addInterceptor(okHttpInterceptor)
        }
    }
}