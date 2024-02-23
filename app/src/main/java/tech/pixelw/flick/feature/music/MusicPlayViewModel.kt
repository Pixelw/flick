package tech.pixelw.flick.feature.music

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.pixelw.flick.core.media.PlayerState
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.feature.music.data.MusicModel
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.math.min

class MusicPlayViewModel : ViewModel(), DefaultLifecycleObserver {

    var currentMediaId: String? = null

    val musicModel = MutableLiveData<MusicModel>()

    private val seekbarAction = MutableLiveData(0.0f)

    val playPosition = MutableLiveData(PlayPosition(0, 0))

    val uiState = MutableLiveData(UiState(PlayerState.IDLE))

    private val timer = Timer()

    private val seekbarUpdateTask = timerTask {
        val position = playPosition.value!!
        position.setPosition(position.getPosition() + POSITION_UPDATE_INTERVAL)
        playPosition.value = position
    }

    fun nextOnClick(view: View) {
        uiState.value = UiState(PlayerState.SKIPPING_TO_NEXT, true)
    }

    fun prevOnClick(view: View) {
        uiState.value = UiState(PlayerState.SKIPPING_TO_PREVIOUS, true)
    }

    fun playPauseOnClick(view: View) {
        uiState.value = when (uiState.value!!.state) {
            PlayerState.PLAYING -> {
                UiState(PlayerState.PAUSED)
            }

            else -> {
                UiState(PlayerState.PLAYING, true)
            }
        }
    }

    fun onSliderChanged(float: Float) {
        if (float < 0f) {
            seekbarUpdateTask.cancel()
        } else if (float <= 1.0f) {
            seekbarAction.value = float
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        seekbarUpdateTask.cancel()
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
        var state: PlayerState
        var isUiTriggered = false

        constructor(value: PlayerState) {
            state = value
        }

        constructor(state: PlayerState, isUiTriggered: Boolean) {
            this.state = state
            this.isUiTriggered = isUiTriggered
        }
    }

    companion object {
        const val POSITION_UPDATE_INTERVAL = 250
    }

}