package tech.pixelw.flick.feature.music

import androidx.databinding.ViewDataBinding
import tech.pixelw.flick.core.ui.BaseActivity

class MusicPlayActivity : BaseActivity<ViewDataBinding>() {

    companion object {
        val K_MUSIC_ID = "key_music_id"
    }

    override val mainFragment = MusicPlayFragment()
}