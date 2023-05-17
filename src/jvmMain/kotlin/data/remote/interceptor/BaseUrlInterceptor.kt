package data.remote.interceptor

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor(
    private val baseUrl: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newUrl = original.url.newBuilder()
            .scheme(baseUrl.toHttpUrlOrNull()!!.scheme)
            .host(baseUrl.toHttpUrlOrNull()!!.host)
            .port(baseUrl.toHttpUrlOrNull()!!.port)
            .build()

        val requestBuilder = original.newBuilder().url(newUrl)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}