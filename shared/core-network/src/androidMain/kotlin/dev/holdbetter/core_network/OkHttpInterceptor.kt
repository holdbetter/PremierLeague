package dev.holdbetter.core_network

import dev.zacsweers.metro.Inject
import okhttp3.Interceptor
import okhttp3.Response

@Inject
internal class OkHttpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        return chain.proceed(request)
    }
}