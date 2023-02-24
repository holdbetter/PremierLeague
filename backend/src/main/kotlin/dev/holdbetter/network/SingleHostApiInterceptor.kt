package dev.holdbetter.network

import dev.holdbetter.innerApi.model.Credit
import dev.holdbetter.routes.Category
import dev.holdbetter.util.asQueryParameter
import io.ktor.http.*
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SingleHostApiInterceptor(
    private val credentials: Credit,
    private val category: Category,
    private val host: String = ApiFootballConfig.HOST,
    private val isHttps: Boolean = true
) : Interceptor {

    private val baseString = URLBuilder().apply {
        protocol = if (isHttps) URLProtocol.HTTPS else URLProtocol.HTTP
        host = this@SingleHostApiInterceptor.host
    }.buildString()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val request = originalRequest
            .newBuilder()
            .setUrl(originalUrl)
            .addCredentials(credentials)
            .build()

        return chain.proceed(request)
    }

    private fun Request.Builder.setUrl(currentUrl: HttpUrl): Request.Builder {
        val urlString = StringBuilder().apply {
            append(baseString)
            append(currentUrl.encodedPath)
            append('?')
            append(category.asQueryParameter())
            append('&')
            append(currentUrl.query)
        }.toString()

        return url(urlString)
    }

    private fun Request.Builder.addCredentials(credentials: Credit): Request.Builder {
        addHeader(ApiFootballConfig.API_KEY_HEADER, credentials.apiKey)
        addHeader(ApiFootballConfig.API_HOST_HEADER, credentials.host)
        return this
    }
}