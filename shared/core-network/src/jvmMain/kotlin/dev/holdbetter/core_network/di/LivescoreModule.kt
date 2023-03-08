package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.LivescoreApiInterceptor
import dev.holdbetter.core_network.OkHttpClientFactory
import dev.holdbetter.core_network.model.Category
import dev.holdbetter.core_network.model.Client
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import dev.shustoff.dikt.Create
import dev.shustoff.dikt.CreateSingle
import dev.shustoff.dikt.UseModules
import kotlinx.serialization.json.Json

@UseModules(DatabaseModule::class)
internal class LivescoreModule(
    private val databaseModule: DatabaseModule
) : ClientModule {

    override val client: Client = Client(RemoteLivescoreConfig.CLIENT)

    override val decoder: Json by lazy {
        Json {
            useAlternativeNames = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }

    override val httpClient by lazy {
        OkHttpClientFactory.createClient {
            addInterceptor(getLivescoreApiInterceptor())
        }
    }

    @Create
    private fun getCategory(): Category

    @CreateSingle
    private fun getLivescoreApiInterceptor(): LivescoreApiInterceptor
}