package dev.holdbetter.core_network

import dev.holdbetter.core_network.di.ClientModule
import dev.holdbetter.core_network.di.DatabaseModule
import dev.holdbetter.core_network.di.LivescoreModule

actual fun getClientModule(): ClientModule = LivescoreModule(DatabaseModule())