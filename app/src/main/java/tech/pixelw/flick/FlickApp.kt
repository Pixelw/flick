package tech.pixelw.flick

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
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

class FlickApp : Application(), ImageLoaderFactory {
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

    override fun newImageLoader(): ImageLoader {
        LogUtil.d("newImageLoader() called")
        return ImageLoader.Builder(this)
            .diskCachePolicy(CachePolicy.ENABLED)
            .callFactory(SharedOkhttpClient.DEFAULT)
            .build()
    }
}