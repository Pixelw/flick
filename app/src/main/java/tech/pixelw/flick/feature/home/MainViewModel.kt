package tech.pixelw.flick.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    fun resetOpenDrawerAction() {
        _drawerOpened.value = false
    }

    private val _drawerOpened = MutableStateFlow(false)

    val drawerOpened = _drawerOpened.asStateFlow()

}