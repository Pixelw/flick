package tech.pixelw.flick.feature.music

import android.content.ComponentName
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.ui.BaseFragment
import tech.pixelw.flick.databinding.FragmentMusicPlayBinding
import tech.pixelw.flick.feature.music.data.MusicListRepository

class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(R.layout.fragment_music_play) {

    private val viewModel by viewModels<MusicPlayViewModel>()

    private var player: Player? = null

    override fun usingBinding() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val musicId = intent?.getStringExtra(MusicPlayActivity.K_MUSIC_ID)
        if (musicId == null) {
            activity?.finish()
            return
        }
        lifecycleScope.launch {
            val musicList = MusicListRepository.getMusicList(MusicListRepository.BANDORI_DEFAULT_PLAY_ROOT)
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
        val controllerFuture = MediaController.Builder(requireContext(), token).buildAsync()
        controllerFuture.addListener({
            player = controllerFuture.get()
            startPlaybackIfNeeded()
        }, MoreExecutors.directExecutor())
    }

    private fun startPlaybackIfNeeded() {
        viewModel.currentMediaId?.let {
            val mediaItem = MusicPlaylistHelper.selectMediaItemById(it)
            if (mediaItem != null) {
                player?.setMediaItem(mediaItem)
            }
        }

    }
}
