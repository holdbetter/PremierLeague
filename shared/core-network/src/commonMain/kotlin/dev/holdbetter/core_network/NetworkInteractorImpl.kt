package dev.holdbetter.core_network

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class NetworkInteractorImpl(
    private val httpClient: HttpClient
) : NetworkInteractor {

    override suspend fun get(vararg paths: String): Flow<String> {
        return flow {
            val response = httpClient.get(ClientEndpoints.CLIENT) {
                url {
                    appendPathSegments(components = paths)
                }
            }.bodyAsText()
            emit(response)
        }.flowOn(Dispatchers.Default)
    }
}