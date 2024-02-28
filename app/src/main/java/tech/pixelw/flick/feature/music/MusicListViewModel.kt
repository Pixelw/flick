package tech.pixelw.flick.feature.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.feature.music.data.MusicListRepository
import tech.pixelw.flick.feature.music.data.MusicModel

class MusicListViewModel : ViewModel() {

    val dataList = MutableLiveData<List<MusicModel>>(emptyList())

    fun loadData() {
        viewModelScope.launch {
            FlickApp.startCronetInitJob?.join()
            kotlin.runCatching {
                LogUtil.d("start first request")
                val musicList = MusicListRepository.getMusicList(MusicListRepository.BANDORI_DEFAULT_PLAY_ROOT)
                dataList.value = musicList
            }.onFailure {
                LogUtil.e(it)
            }

        }
    }

    fun preparePlaylist() {
        MusicPlaylistHelper.setPlaylist(dataList.value!!)
    }

}