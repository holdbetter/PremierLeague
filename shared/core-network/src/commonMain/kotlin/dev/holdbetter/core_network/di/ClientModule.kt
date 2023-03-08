package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.model.Client
import io.ktor.client.*
import kotlinx.serialization.json.Json

interface ClientModule {
    val decoder: Json
    val client: Client
    val httpClient: HttpClient
}