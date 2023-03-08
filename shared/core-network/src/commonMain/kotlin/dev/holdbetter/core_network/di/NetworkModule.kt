package dev.holdbetter.core_network.di

import dev.holdbetter.core_di_api.folder.HasSharedDependencies
import dev.holdbetter.core_network.NetworkInteractor
import dev.holdbetter.core_network.NetworkInteractorImpl
import dev.holdbetter.core_network.getClientModule
import dev.shustoff.dikt.CreateSingle
import dev.shustoff.dikt.UseModules

// TODO: Any ways to test expect-actual method provider?
@UseModules(ClientModule::class)
class NetworkModule(
    private val clientModule: ClientModule = getClientModule()
) : HasSharedDependencies {

    val decoder get() = clientModule.decoder

    val networkInteractor: NetworkInteractor by lazy(::createNetworkInteractor)

    @CreateSingle
    private fun createNetworkInteractor(): NetworkInteractorImpl
}