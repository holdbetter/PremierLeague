package dev.holdbetter.core_network

import dev.holdbetter.core_network.model.Category
import dev.holdbetter.core_network.model.Credit
import dev.holdbetter.core_network.model.Parameter
import dev.holdbetter.core_network.model.RemoteLivescoreConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

// TODO: Test
internal class LivescoreApiInterceptor(
    private val credentials: Credit,
    private val category: Category
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val request = originalRequest
            .newBuilder()
            .addParameters(originalUrl, arrayOf(category))
            .addCredentials(credentials)
            .build()

        return chain.proceed(request)
    }

    private fun Request.Builder.addParameters(
        currentUrl: HttpUrl,
        additionalParameters: Array<Parameter>
    ): Request.Builder {
        val urlString = StringBuilder().apply {
            append("${currentUrl.scheme}://")
            append(currentUrl.host)
            append(currentUrl.encodedPath)
            append('?')
            appendParameters(additionalParameters)
            append('&')
            append(currentUrl.query)
        }.toString()

        return url(urlString)
    }

    private fun StringBuilder.appendParameters(parameters: Array<Parameter>) =
        parameters.map { it.asQueryParameter() }
            .forEach(::append)

    private fun Request.Builder.addCredentials(credentials: Credit): Request.Builder {
        addHeader(RemoteLivescoreConfig.API_KEY_HEADER, credentials.apiKey)
        addHeader(RemoteLivescoreConfig.API_HOST_HEADER, credentials.host)
        return this
    }
}