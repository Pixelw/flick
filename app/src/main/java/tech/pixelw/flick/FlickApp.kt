package tech.pixelw.flick

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import com.google.android.gms.net.CronetProviderInstaller
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.core.network.SharedCronetEngine
import tech.pixelw.flick.core.network.SharedOkhttpClient

class FlickApp : Application(), ImageLoaderFactory {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var appName: String
    }

    override fun onCreate() {
        context = applicationContext
        super.onCreate()
        appName = context.getString(R.string.app_name)
        CronetProviderInstaller.installProvider(context).addOnCompleteListener {
            if (it.isSuccessful) {
                SharedCronetEngine.initSuccess = true
            }
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