package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.NetworkInteractor
import dev.holdbetter.core_network.NetworkInteractorImpl
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import kotlinx.serialization.json.Json

@DependencyGraph
abstract class NetworkGraph {
    abstract val networkInteractor: NetworkInteractor

    abstract val decoder: Json

    @Binds
    internal abstract val NetworkInteractorImpl.bind: NetworkInteractor

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Includes clientModule: ClientModule): NetworkGraph
    }
}