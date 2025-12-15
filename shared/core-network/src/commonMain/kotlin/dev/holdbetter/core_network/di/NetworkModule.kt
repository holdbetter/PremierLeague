package dev.holdbetter.core_network.di

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_di_api.folder.HasSharedDependencies
import dev.holdbetter.core_network.NetworkInteractor
import dev.holdbetter.core_network.NetworkInteractorImpl
import dev.shustoff.dikt.ModuleScopes
import dev.shustoff.dikt.ProvidesMembers
import dev.shustoff.dikt.resolve

// TODO: Any ways to test expect-actual method provider?
@ModuleScopes(Dikt::class)
class NetworkModule(
    @ProvidesMembers private val clientModule: ClientModule
) : HasSharedDependencies {

    val decoder get() = clientModule.decoder

    val networkInteractor: NetworkInteractor by lazy(::createNetworkInteractor)

    private fun createNetworkInteractor(): NetworkInteractorImpl = resolve()
}