package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.LivescoreApiInterceptor
import dev.holdbetter.core_network.OkHttpClientFactory
import dev.holdbetter.core_network.model.Category
import dev.holdbetter.core_network.model.Client
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Named
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import okhttp3.Interceptor

@DependencyGraph(AppScope::class)
abstract class LivescoreGraph : ClientModule {

    @Binds
    internal abstract val LivescoreApiInterceptor.bind: Interceptor

    @Provides
    fun provideClient(@Named("clientName") clientName: String): Client = Client(clientName)

    @Provides
    fun provideCategory(): Category {
        return Category()
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideDecoder(): Json = Json {
        useAlternativeNames = false
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    @Provides
    @Named("clientName")
    fun getClientName(): String {
        return RemoteLivescoreConfig.CLIENT
    }

    @SingleIn(AppScope::class)
    @Provides
    private fun provideHttpClient(livescoreApiInterceptor: Interceptor): HttpClient {
        return OkHttpClientFactory.createClient {
            addInterceptor(livescoreApiInterceptor)
        }
    }

    @DependencyGraph.Factory
    interface Factory {
        fun create(@Includes databaseBindingContainer: DatabaseBindingContainer): LivescoreGraph
    }
}