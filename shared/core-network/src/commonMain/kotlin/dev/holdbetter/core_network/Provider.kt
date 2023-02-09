package dev.holdbetter.core_network

import io.ktor.client.*
import kotlinx.serialization.json.Json

object Provider {
    val decoder = Json {
        ignoreUnknownKeys = true
    }

    val networker: NetworkInteractor = NetworkInteractorImpl(
        httpClient = getHttpClient()
    )
}

expect fun getHttpClient(): HttpClient