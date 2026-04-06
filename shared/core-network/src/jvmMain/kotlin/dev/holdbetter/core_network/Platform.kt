package dev.holdbetter.core_network

import dev.holdbetter.core_network.di.ClientModule
import dev.holdbetter.core_network.di.ClientModuleProvider
import dev.holdbetter.core_network.di.DatabaseModule
import dev.holdbetter.core_network.di.LivescoreModule

class ClientModuleProviderImpl(databaseModule: DatabaseModule) : ClientModuleProvider {
    override val clientModule: ClientModule = LivescoreModule(databaseModule)
}