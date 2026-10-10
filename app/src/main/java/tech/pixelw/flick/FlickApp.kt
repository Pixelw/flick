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
        /** 跟踪 Cronet 安装及共享网络工厂初始化，供网络请求挂起等待。 */
        var networkStackInitJob: Job? = null
            private set
    }

    /** 启动 Cronet 安装，并在安装结束后初始化共享网络工厂和资源配置。 */
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
            val default = SharedOkhttpClient.initialize()
            LogUtil.d(
                "installProvider complete, Call.Factory: ${default.javaClass.simpleName}, costs ${System.currentTimeMillis() - startMillis}ms",
                "CronetInit"
            )
            ResourceHostRepository.fetchHostConfig()
        }
    }

    /** 创建等待网络初始化完成的共享图片加载器，复用网络传输并遵循服务端缓存策略。 */
    @OptIn(ExperimentalCoilApi::class)
    override fun newImageLoader(context: Context): ImageLoader {
        LogUtil.d("newImageLoader() called")
        return ImageLoader.Builder(context)
            .diskCachePolicy(CachePolicy.ENABLED)
            .components {
                add { chain ->
                    // 只挂起图片请求，网络初始化期间界面仍可正常显示。
                    checkNotNull(networkStackInitJob) { "网络初始化尚未启动" }.join()
                    chain.proceed()
                }
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
