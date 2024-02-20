package tech.pixelw.flick.feature.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.pixelw.flick.feature.music.data.MusicModel

class MusicPlayViewModel : ViewModel() {

    val musicModel = MutableLiveData<MusicModel>()

}