package dev.holdbetter.premierleague.android

import android.app.Application
import dev.holdbetter.core_di_api.folder.InjectorOwner
import dev.holdbetter.core_network.di.AndroidClientGraph
import dev.holdbetter.core_network.di.NetworkGraph
import dev.holdbetter.premierleague.android.di.AppGraph
import dev.holdbetter.shared.core_database.di.AndroidDatabaseGraph
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.createGraphFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class PremierLeagueApplication : Application(), InjectorOwner {

    private val appGraph: AppGraph by lazy {
        createGraphFactory<AppGraph.Factory>()
            .create(
                application = this,
                networkGraph = createGraphFactory<NetworkGraph.Factory>().create(
                    clientModule = createGraph<AndroidClientGraph>()
                ),
                databaseGraph = createGraphFactory<AndroidDatabaseGraph.Factory>().create(this),
            )
    }

    override val injectors by appGraph::injectors

    override fun onCreate() {
        super.onCreate()

        initLogger()

        appGraph
    }

    private fun initLogger() {
        Napier.base(DebugAntilog())
    }
}