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
import java.util.TimerTask
import kotlin.concurrent.timerTask
import kotlin.math.min

class MusicPlayViewModel : ViewModel(), DefaultLifecycleObserver {

    var currentMediaId: String? = null

    val musicModel = MutableLiveData<MusicModel>()

    val playPosition = MutableLiveData(PlayPosition(0, 0))

    val uiState = MutableLiveData(PlayerState.IDLE)

    val commandLiveData = MutableLiveData<Command>()

    private var seekbarUpdateTask: TimerTask? = null

    private var timer: Timer? = Timer()

    private fun newTimerTask() = timerTask {
        val position = playPosition.value!!
        position.setPosition(position.getPosition() + POSITION_UPDATE_INTERVAL)
        playPosition.postValue(position)
    }

    fun nextOnClick(view: View) {
        commandLiveData.value = Command(Command.NEXT)
    }

    fun prevOnClick(view: View) {
        commandLiveData.value = Command(Command.PREVIOUS)
    }

    fun playPauseOnClick(view: View) {
        when (uiState.value!!) {
            PlayerState.PLAYING -> {
                commandLiveData.value = Command(Command.PAUSE)
            }

            else -> {
                commandLiveData.value = Command(Command.PLAY)
            }
        }
    }

    fun onSliderChanged(float: Float) {
        if (float < 0f) {
            seekbarUpdateTask?.cancel()
        } else if (float <= 1.0f) {
            commandLiveData.value = Command(Command.SEEK, float)
        }
    }

    fun seekBarPlay(playPosition: PlayPosition) {
        this.playPosition.value = (playPosition)
        seekbarUpdateTask?.cancel()
        seekbarUpdateTask = newTimerTask()
        timer?.scheduleAtFixedRate(seekbarUpdateTask, 0, POSITION_UPDATE_INTERVAL.toLong())
    }

    fun seekBarPause(position: PlayPosition) {
        seekbarUpdateTask?.cancel()
        playPosition.value = position
    }

    override fun onStop(owner: LifecycleOwner) {
        seekbarUpdateTask?.cancel()
    }

    override fun onCleared() {
        // timer
        timer?.cancel()
        timer?.purge()
        timer = null
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

    data class Command(val kind: Int, val payload: Any? = null) {
        companion object {

            const val PLAY = 1

            const val PAUSE = 2

            const val NEXT = 3

            const val PREVIOUS = 4

            const val SELECT_ITEM = 5

            const val SEEK = 6
        }
    }

    companion object {
        const val POSITION_UPDATE_INTERVAL = 250
    }

}