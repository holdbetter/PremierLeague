package dev.holdbetter.core_network

import dev.holdbetter.core_network.di.ClientModule
import dev.holdbetter.core_network.di.ClientModuleImpl
import dev.holdbetter.core_network.di.ClientModuleProvider

class ClientModuleProviderImpl : ClientModuleProvider {
    override val clientModule: ClientModule = ClientModuleImpl()
}
