package tech.pixelw.flick.feature.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.core.misc.LogUtil
import tech.pixelw.flick.feature.music.data.MusicListRepository
import tech.pixelw.flick.feature.music.data.MusicModel

class MusicListViewModel : ViewModel(), MusicPlaylistHelper.Listener {

    val dataList = MutableLiveData<List<MusicModel>>(emptyList())
    val currentPlayIndex = MutableLiveData(-1)

    fun loadData() {
        viewModelScope.launch {
            kotlin.runCatching {
                LogUtil.d("start first request")
                val musicList = MusicListRepository.getMusicList(MusicListRepository.BANDORI_DEFAULT_PLAY_ROOT)
                dataList.value = musicList
            }.onFailure {
                LogUtil.e(it)
            }

        }
        MusicPlaylistHelper.addListener(this)
        if (MusicPlaylistHelper.playIndex > -1) {
            currentPlayIndex.value = MusicPlaylistHelper.playIndex
        }
    }

    fun preparePlaylist() {
        MusicPlaylistHelper.setPlaylist(dataList.value!!)
    }

    override fun onPlayIndexChanged(playIndex: Int, musicModel: MusicModel?) {
        currentPlayIndex.value = playIndex
    }

    override fun onCleared() {
        super.onCleared()
        MusicPlaylistHelper.removeListener(this)
    }
}