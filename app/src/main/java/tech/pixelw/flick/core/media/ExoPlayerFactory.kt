package tech.pixelw.flick.core.media

import androidx.annotation.OptIn
import androidx.media3.common.MediaLibraryInfo
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cronet.CronetDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.core.network.SharedCronetEngine
import java.util.concurrent.Executors

class ExoPlayerFactory {

    companion object {
        @OptIn(UnstableApi::class)
        @JvmStatic
        fun get(): ExoPlayer {
            val dataSource = if (SharedCronetEngine.initSuccess) {
                DefaultDataSource.Factory(
                    FlickApp.context,
                    CronetDataSource.Factory(SharedCronetEngine.DEFAULT, Executors.newSingleThreadExecutor())
                )
            } else {
                DefaultHttpDataSource.Factory()
                    .setUserAgent(
                        "${FlickApp.appName}/${BuildConfig.VERSION_NAME} ${MediaLibraryInfo.VERSION_SLASHY} ${
                            System.getProperty(
                                "http.agent"
                            )
                        }"
                    )
            }
            return ExoPlayer.Builder(FlickApp.context).setMediaSourceFactory(DefaultMediaSourceFactory(dataSource)).build()
        }
    }

}