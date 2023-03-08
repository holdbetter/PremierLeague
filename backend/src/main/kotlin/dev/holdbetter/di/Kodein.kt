package dev.holdbetter.di

import dev.holdbetter.core_network.di.DatabaseModule
import dev.holdbetter.core_network.di.NetworkModule
import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.interactor.DatabaseGateway
import dev.holdbetter.interactor.LeagueDataSource
import dev.holdbetter.interactor.LeagueRepository
import dev.holdbetter.interactor.NetworkGateway
import dev.holdbetter.outerApi.util.LivescoreUnwrapper
import dev.holdbetter.presenter.DatabaseGatewayImpl
import dev.holdbetter.presenter.LeagueDataSourceImpl
import dev.holdbetter.presenter.LeagueRepositoryImpl
import dev.holdbetter.presenter.NetworkGatewayImpl
import io.ktor.server.application.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.kodein.di.*
import org.kodein.di.ktor.di

fun Application.enableKodein() {
    di {
        importAll(
            moduleService(),
            moduleSerialization(),
            moduleRoutes(),
            moduleDatabase(),
            moduleNetwork(),
            moduleApplication()
        )
    }
}

private fun moduleApplication() = DI.Module(name = "application") {
    bind<LeagueDataSource>() with singleton { LeagueDataSourceImpl(instance(), instance()) }
    bind<LeagueRepository>() with singleton { LeagueRepositoryImpl(instance()) }
}

private fun moduleService() = DI.Module(name = "service") {
    bind<CoroutineDispatcher>(tag = "default") with singleton { Dispatchers.Default }
}

private fun moduleSerialization() = DI.Module(name = "serialization") {
    bind<LivescoreUnwrapper>() with singleton(sync = false) { LivescoreUnwrapper() }
    bind<Json>() with singleton(ref = weakReference) {
        Json {
            useAlternativeNames = false
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
    }
}

private fun moduleDatabase() = DI.Module(name = "database") {
    bind<DatabaseModule>() with singleton { DatabaseModule() }
    bind<Database>() with singleton { instance<DatabaseModule>().database }
    bind<DatabaseGateway>() with singleton { DatabaseGatewayImpl(instance("default"), instance()) }
}

private fun moduleNetwork() = DI.Module(name = "network") {
    bind<NetworkModule>() with eagerSingleton { NetworkModule() }
    bind<NetworkGateway>() with singleton {
        with(instance<NetworkModule>()) {
            NetworkGatewayImpl(decoder, networkInteractor, instance())
        }
    }
}

private fun moduleRoutes() = DI.Module(name = "routes") {
    bind<League>() with singleton { League() }
    bind<Country>() with singleton { Country() }
}