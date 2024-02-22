package tech.pixelw.flick.feature.music

import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.ui.BaseFragment
import tech.pixelw.flick.databinding.FragmentMusicPlayBinding

class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(R.layout.fragment_music_play) {

    private val viewModel by viewModels<MusicPlayViewModel>()

    private var player: Player? = null

    private var initLoadJob: Job? = null

    private var startPlayJob: Job? = null

    private lateinit var controllerFuture: ListenableFuture<MediaController>

    override fun usingBinding() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        val musicId = intent?.getStringExtra(MusicPlayActivity.K_MUSIC_ID)
        if (musicId == null) {
            activity?.finish()
            return
        }
        initLoadJob = lifecycleScope.launch {
            val musicList = MusicPlaylistHelper.getPlaylist()
            val musicModel = musicList.find { musicModel -> musicModel.mediaId == musicId }
            if (musicModel != null) {
                viewModel.currentMediaId = musicId
                viewModel.musicModel.value = musicModel
                // 待播放服务链接后再开始播放
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // 链接到播放服务
        val token = SessionToken(requireContext(), ComponentName(requireContext(), MusicPlayService::class.java))
        controllerFuture = MediaController.Builder(requireContext(), token).buildAsync()
        controllerFuture.addListener({
            player = controllerFuture.get()
            playerAddListener()
            startPlaybackIfNeeded()
        }, MoreExecutors.directExecutor())
    }

    private fun playerAddListener() {
        player!!.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val mediaId = mediaItem?.mediaId
                val model = MusicPlaylistHelper.getPlaylist().find { model -> mediaId == model.mediaId } ?: return
                viewModel.musicModel.value = model
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {

            }

            override fun onPlaybackStateChanged(playbackState: Int) {

            }


            override fun onEvents(player: Player, events: Player.Events) {

            }
        })
    }

    override fun onStop() {
        super.onStop()
        MediaController.releaseFuture(controllerFuture)
    }

    private fun startPlaybackIfNeeded() {
        if (startPlayJob != null) return
        if (viewModel.currentMediaId.isNullOrEmpty()) return
        startPlayJob = lifecycleScope.launch {
            initLoadJob?.join()
            val mediaItem = MusicPlaylistHelper.selectMediaItemById(viewModel.currentMediaId!!)
            if (mediaItem != null && player != null) {
                player!!.setMediaItems(MusicPlaylistHelper.getMediaItemList(), MusicPlaylistHelper.playIndex, C.TIME_UNSET)
                player!!.prepare()
                player!!.play()
            }

        }
    }
}
