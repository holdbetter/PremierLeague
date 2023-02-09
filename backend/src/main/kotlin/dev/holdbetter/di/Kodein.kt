package dev.holdbetter.di

import dev.holdbetter.database.DatabaseFactory
import dev.holdbetter.database.dao.CreditDao
import dev.holdbetter.database.dao.CreditDaoImpl
import dev.holdbetter.model.Credit
import dev.holdbetter.network.SingleHostApiInterceptor
import dev.holdbetter.routes.League
import dev.holdbetter.routes.Season
import dev.holdbetter.routes.StandingsApi
import dev.holdbetter.routes.StandingsApiImpl
import dev.holdbetter.util.Mode
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import org.jetbrains.exposed.sql.Database
import org.kodein.di.*
import org.kodein.di.ktor.di

fun Application.enableKodein() {
    di {
        importAll(
            moduleApplication(),
            moduleDatabase(),
            moduleNetwork(),
            moduleRoutes(),
            moduleSerialization()
        )
    }
}

private fun moduleApplication() = DI.Module(name = "application") {
    bind<Mode>() with singleton { Mode(instance<Application>().environment.developmentMode) }
}

private fun moduleSerialization() = DI.Module(name = "serialization") {
    bind<Json>() with singleton(ref = weakReference) {
        Json {
            ignoreUnknownKeys = true
        }
    }
}

private fun moduleDatabase() = DI.Module(name = "database") {
    bind<Credit>() with singleton { instance<CreditDao>().getCredit() }
    bind<Database>() with singleton { DatabaseFactory.init(instance()) }
    bind<CreditDao>() with singleton { CreditDaoImpl(instance()) }
}

private fun moduleNetwork() = DI.Module(name = "network") {
    bind<StandingsApi>() with singleton(sync = false) { StandingsApiImpl(instance(), instance()) }
    bind<Interceptor>() with singleton(sync = false) { SingleHostApiInterceptor(instance()) }
    bind<HttpClient>() with eagerSingleton { httpClient() }
}

private fun moduleRoutes() = DI.Module(name = "routes") {
    bind<Season>() with multiton(ref = softReference) { s: String -> Season(s) }
    bind<League>() with singleton { League() }
}

private fun DirectDI.httpClient() = HttpClient(OkHttp) {
    engine {
        addInterceptor(instance())
    }
}