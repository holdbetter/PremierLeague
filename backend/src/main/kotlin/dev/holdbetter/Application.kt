package dev.holdbetter

import dev.holdbetter.core_network.model.Country
import dev.holdbetter.core_network.model.League
import dev.holdbetter.di.enableKodein
import dev.holdbetter.interactor.LeagueRepository
import dev.holdbetter.plugins.configureRouting
import dev.holdbetter.plugins.configureSerialization
import io.ktor.server.application.*
import kotlinx.coroutines.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

/**
 * Used in configuration file: application.conf
 */
fun Application.module() {
    enableKodein()
    configureSerialization()
    configureRouting()
    onStart()
}

private fun Application.onStart() =
    environment.monitor.subscribe(ApplicationStarted, ::startUpdatingLeagueData)

// TODO: Test
private fun startUpdatingLeagueData(application: Application) {
    val kodein = application.closestDI()
    val repository: LeagueRepository by kodein.instance()
    val league: League by kodein.instance()
    val country: Country by kodein.instance()

    application.launch {
        withContext(Dispatchers.Default) {
            while (isActive) {
                val duration = if (repository.tryDecreaseLimit()) {
                    repository.updateLeagueData(league, country)
                } else {
                    if (repository.hasLimits()) {
                        repository.getDelayDuration()
                    } else {
                        repository.updateLeagueData(league, country)
                    }
                }
                println("NEXT UPDATE PLANNED IN: $duration")
                delay(duration)
            }
        }
    }
}