package tech.pixelw.flick.feature.music

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.ui.BaseFragment
import tech.pixelw.flick.databinding.FragmentMusicPlayBinding
import tech.pixelw.flick.feature.music.data.MusicListRepository

class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(R.layout.fragment_music_play) {

    private val viewModel by viewModels<MusicPlayViewModel>()

    override fun usingBinding() = true

    override fun onStart() {
        super.onStart()
        val musicId = intent?.getStringExtra(MusicPlayActivity.K_MUSIC_ID)
        if (musicId == null) {
            activity?.finish()
            return
        }
        lifecycleScope.launch {
            val musicList = MusicListRepository.getMusicList(MusicListRepository.BANDORI_DEFAULT_PLAY_ROOT)
            val musicModel = musicList.find { musicModel -> musicModel.mediaId == musicId }
            if (musicModel != null) {
                viewModel.musicModel.value = musicModel
            }

        }
    }
}
