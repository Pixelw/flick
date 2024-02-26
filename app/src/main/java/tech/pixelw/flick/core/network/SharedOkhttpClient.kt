package tech.pixelw.flick.core.network

import com.google.net.cronet.okhttptransport.CronetCallFactory
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Response
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.core.misc.LogUtil

object SharedOkhttpClient {

    var preferCronet = true

    /**
     * 如果可用, 将会桥接至Cronet
     */
    val DEFAULT: Call.Factory = getCallFactory()

    private fun getCallFactory(): Call.Factory {
        if (preferCronet && SharedCronetEngine.initSuccess && SharedCronetEngine.DEFAULT != null) {
            try {
                return CronetCallFactory.newBuilder(SharedCronetEngine.DEFAULT).build()
            } catch (t: Throwable) {
                LogUtil.e("init Cronet failed", t)
            }
        }
        val builder = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor("Default"))
        return builder.build()
    }

    class HeaderInterceptor(okhttpRemark: String) : Interceptor {
        private val customUserAgent =
            "${FlickApp.appName}/${BuildConfig.VERSION_NAME} Okhttp/${OkHttp.VERSION} ($okhttpRemark) ${System.getProperty("http.agent")}"

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .header("User-Agent", customUserAgent)
                .build()
            return chain.proceed(request)
        }

    }
}