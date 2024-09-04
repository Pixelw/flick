package tech.pixelw.flick.feature.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tech.pixelw.flick.feature.station.bandori.data.BsBaseEntity

class StationViewModel : ViewModel() {
    private val dataRepository = StationDataRepository()
    val dataList = MutableStateFlow(listOf<BsBaseEntity>())
    var myUid: String = ""
        private set

    fun connect() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.connectToWs { comingList, action ->
                val list = ArrayList(dataList.value)
                list.addAll(comingList)
                list.sortBy { it.timestamp }
                dataList.value = list
            }
//            val roomNumber = dataRepository.getRoomNumber()
//            dataList.value = roomNumber
        }
    }

}