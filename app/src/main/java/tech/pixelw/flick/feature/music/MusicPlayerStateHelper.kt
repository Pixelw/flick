package tech.pixelw.flick.feature.music

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import tech.pixelw.flick.core.media.PlayerState

class MusicPlayerStateHelper(private val viewModel: MusicPlayViewModel, private val player: Player) : Player.Listener {

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        val mediaId = mediaItem?.mediaId
        val model = MusicPlaylistHelper.getPlaylist().find { model -> mediaId == model.mediaId } ?: return
        viewModel.musicModel.value = model

//        viewModel.uiState.
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        val state = if (!isPlaying) {
            if (player.playbackState == Player.STATE_READY) {
                PlayerState.PAUSED
            } else PlayerState.ENDED
        } else {
            PlayerState.PLAYING
        }
        edit(state)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                edit(PlayerState.IDLE)
            }

            Player.STATE_READY -> {

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
        val value = viewModel.uiState.value!!
        value.state = playerState
        viewModel.uiState.value = value
    }

}