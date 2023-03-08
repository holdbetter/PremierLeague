package dev.holdbetter.core_network

import dev.holdbetter.core_network.di.ClientModule
import dev.holdbetter.core_network.di.ClientModuleImpl

actual fun getClientModule(): ClientModule = ClientModuleImpl()