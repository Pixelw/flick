package tech.pixelw.flick.feature.music

import android.app.PendingIntent
import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import tech.pixelw.flick.core.media.ExoPlayerFactory

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
        player = ExoPlayerFactory.get()
        player!!.addListener(playerListener)
        player!!.setAudioAttributes(
            AudioAttributes.Builder().setContentType(C.AUDIO_CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA)
                .setAllowedCapturePolicy(C.ALLOW_CAPTURE_BY_NONE).build(), true
        )
        mediaSession = MediaSession.Builder(this, player!!).setSessionActivity(
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, MusicPlayActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        ).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
//        if (controllerInfo.packageName != BuildConfig.APPLICATION_ID) return null
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player!!
        if (!player.playWhenReady || player.mediaItemCount == 0) {
            stopSelf()
        }
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