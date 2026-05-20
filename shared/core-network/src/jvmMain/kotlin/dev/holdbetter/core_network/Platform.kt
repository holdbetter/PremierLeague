package dev.holdbetter.core_network

import dev.holdbetter.core_network.di.ClientModule
import dev.holdbetter.core_network.di.ClientModuleProvider
import dev.holdbetter.core_network.di.DatabaseBindingContainer
import dev.holdbetter.core_network.di.LivescoreGraph
import dev.zacsweers.metro.createGraphFactory

class ClientModuleProviderImpl(databaseBindingContainer: DatabaseBindingContainer) : ClientModuleProvider {
    override val clientModule: ClientModule =
        createGraphFactory<LivescoreGraph.Factory>()
            .create(databaseBindingContainer)
}