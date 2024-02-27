package tech.pixelw.flick.feature.music

import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import tech.pixelw.flick.core.media.PlayerState

class MusicPlayerStateHelper(private val viewModel: MusicPlayViewModel, private val player: Player) : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        Log.d(TAG, "onMediaItemTransition() called with: mediaItem = $mediaItem, reason = $reason")
        super.onMediaItemTransition(mediaItem, reason)
        val mediaId = mediaItem?.mediaId
        val model = MusicPlaylistHelper.getPlaylist().find { model -> mediaId == model.mediaId } ?: return
        viewModel.musicModel.value = model

//        viewModel.uiState.
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        Log.d(TAG, "onIsPlayingChanged() called with: isPlaying = $isPlaying")
        val state = if (!isPlaying) {
            if (player.playbackState == Player.STATE_READY) {
                viewModel.seekBarPause(MusicPlayViewModel.PlayPosition(player.currentPosition, player.duration))
                PlayerState.PAUSED
            } else {
                PlayerState.ENDED
            }
        } else {
            viewModel.seekBarPlay(MusicPlayViewModel.PlayPosition(player.currentPosition, player.duration))
            PlayerState.PLAYING
        }
        edit(state)
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