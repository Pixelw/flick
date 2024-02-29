package tech.pixelw.flick.feature.music

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import tech.pixelw.flick.core.media.PlayerState
import tech.pixelw.flick.core.misc.LogUtil

class MusicPlayerStateHelper(private val viewModel: MusicPlayViewModel, private val player: Player) : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.d(TAG, "onMediaItemTransition() called with: mediaItem = $mediaItem, reason = $reason")
        super.onMediaItemTransition(mediaItem, reason)
        val mediaId = mediaItem?.mediaId
        mediaId?.let {
            viewModel.musicModel.value = MusicPlaylistHelper.selectMusicById(it)
        }
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d(TAG, "onIsPlayingChanged() called with: isPlaying = $isPlaying")
        if (!isPlaying) {
            if (player.playbackState == Player.STATE_READY) {
                viewModel.seekBarPause(MusicPlayViewModel.PlayPosition(player.currentPosition, player.duration))
                edit(PlayerState.PAUSED)
            } else if (player.playbackState == Player.STATE_ENDED) {
                edit(PlayerState.ENDED)
            }
        } else {
            viewModel.seekBarPlay(MusicPlayViewModel.PlayPosition(player.currentPosition, player.duration))
            edit(PlayerState.PLAYING)
        }

    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        Log.d(TAG, "onPlaybackStateChanged() called with: playbackState = $playbackState")
        when (playbackState) {
            Player.STATE_IDLE -> {
                edit(PlayerState.IDLE)
            }

            Player.STATE_READY -> {
                // onIsPlayingChanged
            }

            Player.STATE_BUFFERING -> {
                edit(PlayerState.BUFFERING)
            }

            Player.STATE_ENDED -> {
                edit(PlayerState.ENDED)
            }
        }
    }

    private fun edit(playerState: PlayerState) {
        LogUtil.d("playerstate edit: $playerState", TAG)
        viewModel.uiState.value = playerState
    }

    companion object {
        private const val TAG = "MusicPlayerStateHelper"
    }

}

fun Player.getPlayerState(): PlayerState {
    return when (this.playbackState) {
        Player.STATE_IDLE -> {
            PlayerState.IDLE
        }

        Player.STATE_READY -> {
            if (isPlaying) PlayerState.PLAYING else PlayerState.PAUSED
        }

        Player.STATE_BUFFERING -> {
            PlayerState.BUFFERING
        }

        else -> {
            PlayerState.ENDED
        }
    }
}