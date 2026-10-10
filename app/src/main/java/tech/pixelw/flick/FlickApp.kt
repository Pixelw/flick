package tech.pixelw.flick

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.network.cachecontrol.CacheControlCacheStrategy
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.CachePolicy
import com.google.android.gms.net.CronetProviderInstaller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import tech.pixelw.flick.common.resources.ResourceHostRepository
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.core.network.SharedCronetEngine
import tech.pixelw.flick.core.network.SharedOkhttpClient
import kotlin.coroutines.resume

class FlickApp : Application(), SingletonImageLoader.Factory {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var appName: String
        var networkStackInitJob: Job? = null
            private set
    }

    override fun onCreate() {
        context = applicationContext
        super.onCreate()
        appName = context.getString(R.string.app_name)
        networkStackInitJob = MainScope().launch(Dispatchers.Default) {
            LogUtil.d("installProvider start", "CronetInit")
            val startMillis = System.currentTimeMillis()
            suspendCancellableCoroutine { cont ->
                CronetProviderInstaller.installProvider(context).addOnCompleteListener {
                    if (it.isSuccessful) {
                        SharedCronetEngine.initSuccess = true
                    }
                    LogUtil.d("installProvider complete, successful=${it.isSuccessful}", "CronetInit")
                    cont.resume(it.isSuccessful)
                }
            }
            val default = SharedOkhttpClient.DEFAULT
            LogUtil.d(
                "installProvider complete, Call.Factory: ${default.hashCode()}, costs ${System.currentTimeMillis() - startMillis}ms",
                "CronetInit"
            )
            ResourceHostRepository.fetchHostConfig()
        }
    }

    /** 创建共享图片加载器，复用网络传输并遵循服务端缓存策略。 */
    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context): ImageLoader {
        LogUtil.d("newImageLoader() called")
        return ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = { SharedOkhttpClient.DEFAULT },
                        cacheStrategy = { CacheControlCacheStrategy() }
                    )
                )
            }
            .build()
    }
}
