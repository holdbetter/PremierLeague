package dev.holdbetter.core_network

import dev.holdbetter.core_network.model.Client
import dev.holdbetter.core_network.model.Parameter
import dev.zacsweers.metro.Inject
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.appendPathSegments

@Inject
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