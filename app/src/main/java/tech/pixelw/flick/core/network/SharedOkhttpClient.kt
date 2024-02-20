package tech.pixelw.flick.core.network

import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Response
import tech.pixelw.flick.FlickApp

object SharedOkhttpClient {

    val DEFAULT = OkHttpClient.Builder()
        .addInterceptor(HeaderInterceptor("SingletonDefault"))
        .build()

    class HeaderInterceptor(okhttpRemark: String) : Interceptor {
        private val customUserAgent =
            "${FlickApp.appName} Okhttp/${OkHttp.VERSION} ($okhttpRemark) ${System.getProperty("http.agent")}"

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .header("User-Agent", customUserAgent)
                .build()
            return chain.proceed(request)
        }

    }
}