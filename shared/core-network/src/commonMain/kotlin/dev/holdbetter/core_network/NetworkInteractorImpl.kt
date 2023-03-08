package dev.holdbetter.core_network

import dev.holdbetter.core_network.model.Client
import dev.holdbetter.core_network.model.Parameter
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

internal class NetworkInteractorImpl(
    private val client: Client,
    private val httpClient: HttpClient
) : NetworkInteractor {

    // TODO: Test
    override suspend fun get(
        paths: Array<String>,
        vararg parameters: Parameter
    ): String {
        return httpClient.get(client.value) {
            url {
                appendPathSegments(components = paths)
                parameters.forEach(this.parameters::add)
            }
        }.bodyAsText()
    }
}