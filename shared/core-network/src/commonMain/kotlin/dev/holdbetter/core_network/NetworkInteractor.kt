package dev.holdbetter.core_network

import dev.holdbetter.core_network.model.Parameter

interface NetworkInteractor {
    suspend fun get(paths: Array<String>, vararg parameters: Parameter): String
}