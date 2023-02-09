package dev.holdbetter.core_network

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

actual fun getHttpClient() = HttpClient(OkHttp) {
    engine {
        addInterceptor(OkHttpInterceptor())
    }
}