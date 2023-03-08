package dev.holdbetter.core_network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

internal object DarwinHttpClientFactory : ClientFactory<DarwinClientEngineConfig> {
    override fun createClient(block: DarwinClientEngineConfig.() -> Unit) =
        HttpClient(Darwin.create(block))
}