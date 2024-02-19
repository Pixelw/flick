package tech.pixelw.flick.music

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import tech.pixelw.flick.R
import tech.pixelw.flick.common.ui.BaseFragment
import tech.pixelw.flick.databinding.FragmentMusicPlayBinding

class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(R.layout.fragment_music_play) {

    private val viewModel by viewModels<MusicPlayViewModel>()

    override fun usingBinding() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}
