package tech.pixelw.flick.feature.music

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.databinding.ViewDataBinding
import tech.pixelw.flick.core.ui.BaseActivity

class MusicPlayActivity : BaseActivity<ViewDataBinding>() {

    /** 创建播放器窗口，并让页面背景延伸到系统栏区域。 */
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    companion object {
        val K_MUSIC_ID = "key_music_id"
    }

    override val mainFragment = MusicPlayFragment()
}
