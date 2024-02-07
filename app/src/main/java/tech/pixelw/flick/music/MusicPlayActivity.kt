package tech.pixelw.flick.music

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import tech.pixelw.flick.R

class MusicPlayActivity : AppCompatActivity() {

    private val viewModel by viewModels<MusicPlayViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_play)

    }
}