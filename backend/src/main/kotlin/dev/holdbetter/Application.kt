package dev.holdbetter

import dev.holdbetter.di.enableKodein
import dev.holdbetter.plugins.configureRouting
import dev.holdbetter.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    enableKodein()
    configureSerialization()
    configureRouting()
}