package dev.holdbetter.core_network

import io.ktor.client.*
import io.ktor.client.engine.*

// TODO: Test
interface ClientFactory<T : HttpClientEngineConfig> {
    fun createClient(block: T.() -> Unit = {}): HttpClient
}