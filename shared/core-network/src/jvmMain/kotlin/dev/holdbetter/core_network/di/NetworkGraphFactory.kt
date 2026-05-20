package dev.holdbetter.core_network.di

import dev.zacsweers.metro.createGraphFactory

object NetworkGraphFactory {
    fun create(clientModule: ClientModule): NetworkGraph {
        return createGraphFactory<NetworkGraph.Factory>().create(clientModule)
    }
}