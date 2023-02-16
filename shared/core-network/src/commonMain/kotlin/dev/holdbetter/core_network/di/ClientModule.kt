package dev.holdbetter.core_network.di

import dev.holdbetter.core_network.getHttpClient

class ClientModule {
    val httpClient by lazy(::getHttpClient)
}