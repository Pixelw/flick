package tech.pixelw.flick.feature.music

import android.content.ComponentName
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.core.ui.BaseFragment
import tech.pixelw.flick.databinding.FragmentMusicPlayBinding

class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(R.layout.fragment_music_play) {

    private val viewModel by viewModels<MusicPlayViewModel>()

    private var player: Player? = null

    private var pendingStartMediaId: String? = null

    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var stateHelper: MusicPlayerStateHelper? = null

    override fun usingBinding() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        binding.sliderPlayer.setLabelFormatter { value ->
            DateUtils.formatElapsedTime(((viewModel.playPosition.value?.duration ?: 0L) * value).toLong() / 1000)
        }
        lifecycle.addObserver(viewModel)
        pendingStartMediaId = intent?.getStringExtra(MusicPlayActivity.K_MUSIC_ID)
        viewModel.commandLiveData.observe(viewLifecycleOwner) {
            when (it?.kind) {
                MusicPlayViewModel.Command.PLAY -> {
                    player?.play()
                }

                MusicPlayViewModel.Command.PAUSE -> {
                    player?.pause()
                }

                MusicPlayViewModel.Command.NEXT -> {
                    player?.seekToNext()
                    player?.play()
                }

                MusicPlayViewModel.Command.PREVIOUS -> {
                    player?.seekToPrevious()
                    player?.play()
                }

                MusicPlayViewModel.Command.SEEK -> {
                    player?.seekTo((player!!.duration * it.payload as Float).toLong())
                }

            }
        }
    }

    override fun onStart() {
        super.onStart()
        // 链接到播放服务
        val token = SessionToken(requireContext(), ComponentName(requireContext(), MusicPlayService::class.java))
        controllerFuture = MediaController.Builder(requireContext(), token).buildAsync()
        controllerFuture.addListener({
            // 服务已连接后
            player = controllerFuture.get()
            if (player == null) {
                LogUtil.e("Player is null!", TAG)
                return@addListener
            }
            playerAddListener()
            startPlaybackOrGetNowPlaying()
            refreshPlayState()
        }, MoreExecutors.directExecutor())
    }

    private fun playerAddListener() {
        stateHelper = MusicPlayerStateHelper(viewModel, player!!)
        player!!.addListener(stateHelper!!)
    }

    private fun refreshPlayState() {
        if (player == null) return
        viewModel.uiState.value = player!!.getPlayerState()
        val playPosition = MusicPlayViewModel.PlayPosition(player!!.currentPosition, player!!.duration)
        if (player!!.isPlaying) {
            viewModel.seekBarPlay(playPosition)
        } else {
            viewModel.seekBarPause(playPosition)
        }
    }

    private fun startPlaybackOrGetNowPlaying() {
        if (pendingStartMediaId.isNullOrEmpty()) {
            MusicPlaylistHelper.getCurrentMusicModel()?.let {
                viewModel.musicModel.value = it
            }
        } else {
            lifecycleScope.launch {
                val musicModel = MusicPlaylistHelper.selectMusicById(pendingStartMediaId!!)
                if (musicModel != null) {
                    viewModel.musicModel.value = musicModel
                    player!!.setMediaItems(MusicPlaylistHelper.getMediaItemList(), MusicPlaylistHelper.playIndex, C.TIME_UNSET)
                    player!!.prepare()
                    player!!.play()
                }
                pendingStartMediaId = null
            }
        }

    }

    override fun onStop() {
        super.onStop()
        stateHelper?.let { player?.removeListener(it) }
        player = null
        MediaController.releaseFuture(controllerFuture)
    }

    companion object {
        private const val TAG = "MusicPlayFragment"
    }
}
