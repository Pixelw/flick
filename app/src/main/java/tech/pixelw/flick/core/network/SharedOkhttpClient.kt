package tech.pixelw.flick.core.network

import android.util.Log
import com.google.net.cronet.okhttptransport.CronetCallFactory
import okhttp3.Cache
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

    val fallbackOkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor("Default"))
            .cache(Cache(FlickApp.context.cacheDir, 256 * 1024 * 1024))
        builder.build()
    }

    @Volatile
    private var initializedCallFactory: Call.Factory? = null

    /** 返回初始化完成后确定的共享网络工厂；初始化前访问会抛出异常。 */
    val DEFAULT: Call.Factory
        get() = checkNotNull(initializedCallFactory) { "共享网络工厂尚未完成初始化" }

    /** 在 Cronet 安装结束后创建共享网络工厂，重复调用时复用已创建的实例。 */
    @Synchronized
    fun initialize(): Call.Factory {
        return initializedCallFactory ?: getCallFactory().also { initializedCallFactory = it }
    }

    private fun getCallFactory(): Call.Factory {
        Log.d(TAG, "getCallFactory() called")
        val engine = if (preferCronet && SharedCronetEngine.initSuccess) SharedCronetEngine.getEngine() else null
        if (engine != null) {
            try {
                return CronetCallFactory.newBuilder(engine).build()
            } catch (t: Throwable) {
                LogUtil.e("init Cronet failed", t)
            }
        }
        return fallbackOkHttpClient

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

    private const val TAG = "SharedOkhttpClient"
}
