package dev.holdbetter.core_network

import kotlinx.coroutines.flow.Flow

interface NetworkInteractor {
    suspend fun get(vararg paths: String) : Flow<String>
}