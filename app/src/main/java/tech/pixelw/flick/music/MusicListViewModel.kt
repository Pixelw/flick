package tech.pixelw.flick.music

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.pixelw.flick.common.misc.LogUtil
import tech.pixelw.flick.music.data.MusicListRepository
import tech.pixelw.flick.music.data.MusicModel

class MusicListViewModel : ViewModel() {

    val repository = MusicListRepository()
    val dataList = MutableLiveData<List<MusicModel>>()

    fun loadData() {
        viewModelScope.launch {
            kotlin.runCatching {
                val musicList = repository.getMusicList()
                dataList.value = musicList
            }.onFailure {
                LogUtil.e(it)
            }

        }
    }

}