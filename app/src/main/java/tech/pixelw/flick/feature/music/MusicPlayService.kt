package tech.pixelw.flick.feature.music

import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaLibraryInfo
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp

class MusicPlayService : MediaSessionService() {

    private var mediaSession: MediaSession? = null
    private var player: ExoPlayer? = null
    private val playerListener = object : Player.Listener {
        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            player ?: return
            MusicPlaylistHelper.playIndex = player!!.currentMediaItemIndex
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()
        val dataSource = DefaultHttpDataSource.Factory()
            .setUserAgent(
                "${FlickApp.appName}/${BuildConfig.VERSION_NAME} ${MediaLibraryInfo.VERSION_SLASHY} ${
                    System.getProperty(
                        "http.agent"
                    )
                }"
            )
        player = ExoPlayer.Builder(this).setMediaSourceFactory(DefaultMediaSourceFactory(dataSource)).build()
        player!!.addListener(playerListener)
        mediaSession = MediaSession.Builder(this, player!!).build()

    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
//        if (controllerInfo.packageName != BuildConfig.APPLICATION_ID) return null
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.removeListener(playerListener)
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}