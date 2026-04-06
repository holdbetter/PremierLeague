package dev.holdbetter.core_network

import dev.holdbetter.core_di_api.folder.Dikt
import dev.shustoff.dikt.InjectableSingleInScope
import okhttp3.Interceptor
import okhttp3.Response

internal class OkHttpInterceptor : Interceptor, InjectableSingleInScope<Dikt> {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()
        return chain.proceed(request)
    }
}