package tech.pixelw.flick.core.network

import org.chromium.net.ApiVersion
import org.chromium.net.CronetEngine
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.core.misc.LogUtil

object SharedCronetEngine {

    var initSuccess = false

    val DEFAULT: CronetEngine? = getEngine()

    private fun getEngine(): CronetEngine? {
        try {
            LogUtil.d("getEngine() start")
            CronetEngine.Builder(FlickApp.context)
                .setUserAgent(
                    "${FlickApp.appName}/${BuildConfig.VERSION_NAME} Cronet/${ApiVersion.getCronetVersion()} (Default) ${
                        System.getProperty("http.agent")
                    }"
                )
                .setStoragePath(FlickApp.context.cacheDir.absolutePath)
                .enableHttpCache(CronetEngine.Builder.HTTP_CACHE_DISK, 256 * 1024 * 1024)
                .build()
        } catch (t: Throwable) {
            LogUtil.w("getEngine() failed", t, TAG)
        }
        return null
    }

    private const val TAG = "SharedCronetEngine"


}