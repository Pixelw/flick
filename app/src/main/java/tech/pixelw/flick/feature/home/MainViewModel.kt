package tech.pixelw.flick.feature.home

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.R
import tech.pixelw.flick.core.extension.dataStore

class MainViewModel : ViewModel() {

    data class ScreenTitleModel(val title: String, val upperTitle: String = "", val lowerTitle: String = "")

    val currentTitleModel = MutableStateFlow(
        ScreenTitleModel(
            title = FlickApp.context.getString(R.string.welcome_title),
            upperTitle = FlickApp.context.getString(
                R.string.version_x, BuildConfig.VERSION_NAME
            )
        )
    )

    fun resetOpenDrawerAction() {
        _drawerOpened.value = false
    }

    fun getPreferScreen(context: Context) {
        context.dataStore.data.map {
            it[SCREEN_PREF]
        }
    }

    private val _drawerOpened = MutableStateFlow(false)

    val drawerOpened = _drawerOpened.asStateFlow()

    companion object PreferencesKeys {
        val SCREEN_PREF = stringPreferencesKey("screen_pref")
    }

}