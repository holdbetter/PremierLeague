package dev.holdbetter.core_network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual fun getHttpClient() = HttpClient(Darwin)