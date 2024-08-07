package tech.pixelw.flick.feature.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import tech.pixelw.flick.feature.station.bandori.data.BsRoom

class StationViewModel : ViewModel() {
    private val dataRepository = StationDataRepository()
    val dataList = MutableStateFlow(listOf<BsRoom>())
    var myUid: String = ""
        private set

    fun loadData() {
        viewModelScope.launch {
            val roomNumber = dataRepository.getRoomNumber()
            dataList.value = roomNumber
        }
    }

}