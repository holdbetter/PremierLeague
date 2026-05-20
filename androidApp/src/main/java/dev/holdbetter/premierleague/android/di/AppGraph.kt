package dev.holdbetter.premierleague.android.di

import android.app.Application
import android.content.Context
import dev.holdbetter.core_di_api.folder.InjectorOwner
import dev.holdbetter.core_di_api.folder.InjectorScope
import dev.holdbetter.core_network.di.NetworkGraph
import dev.holdbetter.shared.core_database.di.DatabaseGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides

@DependencyGraph(scope = AppScope::class, additionalScopes = [InjectorScope::class])
interface AppGraph : InjectorOwner {

    @Provides
    fun provideApplicationContext(application: Application): Context = application

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides application: Application,
            @Includes networkGraph: NetworkGraph,
            @Includes databaseGraph: DatabaseGraph
        ): AppGraph
    }
}