package tech.pixelw.flick.feature.home

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import tech.pixelw.flick.BuildConfig
import tech.pixelw.flick.FlickApp
import tech.pixelw.flick.R
import tech.pixelw.flick.core.extension.dataStore
import tech.pixelw.flick.core.extension.toast
import tech.pixelw.flick.feature.home.composables.MainNavDrawerEntrance

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

    val currentSelectedEntrance = MutableStateFlow(MainNavDrawerEntrance.BD_STATION)

    val drawerStateFlow = MutableStateFlow(DrawerState(DrawerValue.Closed))

    val firstShow = MutableStateFlow(true)

    suspend fun openDrawer() {
//        _drawerShouldBeOpened.value = true
        val drawerState1 = drawerStateFlow.value
        drawerState1.open()
//        drawerStateFlow.value = drawerState1
    }

    suspend fun closeDrawer() {
//        _drawerShouldBeOpened.value = false
        val drawerState = drawerStateFlow.value
        drawerState.close()
//        drawerStateFlow.value = drawerState
    }

    fun getPreferScreen(context: Context) {
        val map = context.dataStore.data.map {
            it[SCREEN_PREF]
        }
    }

    suspend fun switchScreen(navController: NavController, mainNavDrawerEntrance: MainNavDrawerEntrance) {
        when (mainNavDrawerEntrance) {
            MainNavDrawerEntrance.BD_STATION -> {
                navController.navigate(R.id.screen_station)
            }

            MainNavDrawerEntrance.APP_MUSIC_PLAYER -> {
                navController.navigate(R.id.screen_music_list)
            }

            else -> {
                toast("Not implemented yet...")
                return
            }
        }
        currentSelectedEntrance.value = mainNavDrawerEntrance
        setupTitle()
        closeDrawer()
    }

    fun setupTitle() {
        val upperTitle: String = when (currentSelectedEntrance.value.group) {
            1 -> {
                FlickApp.context.getString(R.string.bangdream_gbp)
            }

            2 -> {
                FlickApp.context.getString(R.string.pj_sekai)
            }

            else -> ""
        }
        val title = FlickApp.context.getString(currentSelectedEntrance.value.stringId)
        currentTitleModel.value = ScreenTitleModel(title = title, upperTitle = upperTitle)
    }

//    private val _drawerShouldBeOpened = MutableStateFlow(false)
//
//    val drawerShouldOpened = _drawerShouldBeOpened.asStateFlow()

    companion object PreferencesKeys {
        val SCREEN_PREF = stringPreferencesKey("screen_pref")
    }

}