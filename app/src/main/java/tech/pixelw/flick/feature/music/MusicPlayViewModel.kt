package tech.pixelw.flick.feature.music

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.feature.music.data.MusicModel
import kotlin.math.min

class MusicPlayViewModel : ViewModel() {

    var currentMediaId: String? = null

    val musicModel = MutableLiveData<MusicModel>()

    val playPosition = MutableLiveData(PlayPosition(0, 0))

    fun nextOnClick(view: View) {

    }

    fun prevOnClick(view: View) {

    }

    fun playPauseOnClick(view: View) {

    }

    fun onSliderChanged(float: Float) {

    }


    class PlayPosition {
        val duration: Int

        // millis
        private var position: Int

        constructor(position: Int, duration: Int) {
            this.position = position
            this.duration = duration
        }

        constructor(position: Long, duration: Long) {
            this.duration = duration.toInt()
            this.position = position.toInt()
        }

        fun getPosition(): Int {
            return position
        }

        fun setPosition(position: Int) {
            var position1 = position
            if (position1 > duration) {
                LogUtil.w("position out of bounds")
                position1 = duration
            }
            this.position = position1
        }

        val seekbarPos: Float
            get() {
                if (duration == 0) return 0.0f
                val v = position.toFloat() / duration.toFloat()
                return min(v, 1.0f)
            }
    }


    class UiState {
        var state: Int
        var isUiTriggered = false

        constructor(value: Int) {
            state = value
        }

        constructor(state: Int, isUiTriggered: Boolean) {
            this.state = state
            this.isUiTriggered = isUiTriggered
        }
    }


}