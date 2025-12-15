package dev.holdbetter.core_network.di

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.LivescoreApiInterceptor
import dev.holdbetter.core_network.OkHttpClientFactory
import dev.holdbetter.core_network.model.Category
import dev.holdbetter.core_network.model.Client
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import dev.shustoff.dikt.ModuleScopes
import dev.shustoff.dikt.ProvidesMembers
import dev.shustoff.dikt.resolve
import kotlinx.serialization.json.Json

@ModuleScopes(Dikt::class)
internal class LivescoreModule(
    @ProvidesMembers private val databaseModule: DatabaseModule
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

    private fun getCategory(): Category = resolve()

    private fun getLivescoreApiInterceptor(): LivescoreApiInterceptor = resolve()
}