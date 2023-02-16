package dev.holdbetter.core_network.di

import dev.holdbetter.core_di_api.folder.HasSharedDependencies
import dev.holdbetter.core_network.NetworkInteractor
import dev.holdbetter.core_network.NetworkInteractorImpl
import dev.shustoff.dikt.CreateSingle
import dev.shustoff.dikt.UseModules
import kotlinx.serialization.json.Json

@UseModules(ClientModule::class)
class NetworkModule(
    private val clientModule: ClientModule = ClientModule()
) : HasSharedDependencies {

    val decoder by lazy(::getJsonDecoder)

    val networkInteractor: NetworkInteractor by lazy(::createNetworkInteractor)

    @CreateSingle
    private fun createNetworkInteractor(): NetworkInteractorImpl

    private fun getJsonDecoder() = Json {
        ignoreUnknownKeys = true
    }
}