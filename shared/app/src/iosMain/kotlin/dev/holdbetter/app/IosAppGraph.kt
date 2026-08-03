package dev.holdbetter.app

import dev.holdbetter.compose.feature_standings.StandingsRoute
import dev.holdbetter.core_network.di.NetworkGraph
import dev.holdbetter.shared.core_database.di.DatabaseGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes

@DependencyGraph(scope = AppScope::class)
interface IosAppGraph {

    val standingsRoute: StandingsRoute

    @DependencyGraph.Factory
    interface Factory {
        fun create(
            @Includes networkGraph: NetworkGraph,
            @Includes databaseGraph: DatabaseGraph
        ): IosAppGraph
    }
}