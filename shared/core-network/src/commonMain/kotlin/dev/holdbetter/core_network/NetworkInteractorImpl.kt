package dev.holdbetter.core_network

import dev.holdbetter.core_di_api.folder.Dikt
import dev.holdbetter.core_network.model.Client
import dev.holdbetter.core_network.model.Parameter
import dev.shustoff.dikt.InjectableSingleInScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.appendPathSegments

internal class NetworkInteractorImpl(
    private val client: Client,
    private val httpClient: HttpClient
) : NetworkInteractor, InjectableSingleInScope<Dikt> {

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