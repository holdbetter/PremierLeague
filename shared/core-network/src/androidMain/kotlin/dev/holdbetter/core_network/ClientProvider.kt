package dev.holdbetter.core_network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

internal object OkHttpClientFactory : ClientFactory<OkHttpConfig> {
    override fun createClient(block: OkHttpConfig.() -> Unit) = HttpClient(OkHttp.create(block))
}