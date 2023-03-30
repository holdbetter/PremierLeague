package dev.holdbetter.premierleague.android

import android.app.Application
import dev.holdbetter.core_di_impl.ModuleDependencies
import dev.holdbetter.core_di_impl.ModuleProvider
import dev.holdbetter.core_di_impl.MutableModuleDependencies
import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.premierleague.android.di.AppModule
import dev.holdbetter.shared.core_database.database.LeagueUserDatabase
import dev.holdbetter.shared.core_database.di.DatabaseModule
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class PremierLeagueApplication : Application(), ModuleProvider {

    private val _module: MutableModuleDependencies = mutableMapOf()
    override val moduleMap: ModuleDependencies = _module

    override fun onCreate() {
        super.onCreate()

        initLogger()

        AppModule.Factory.create(
            networkModule = NetworkModule(),
            databaseModule = DatabaseModule(LeagueUserDatabase.getInstance(this)),
            dependencies = _module
        )
    }

    private fun initLogger() {
        Napier.base(DebugAntilog())
    }
}