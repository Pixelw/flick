package tech.pixelw.flick.feature.home

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.view.ViewCompat
import androidx.navigation.findNavController
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.extension.toast
import tech.pixelw.flick.databinding.ContentMainBinding
import tech.pixelw.flick.feature.home.composables.MainContentFrame
import tech.pixelw.flick.feature.home.composables.MainNavDrawer
import tech.pixelw.flick.feature.home.composables.MainNavDrawerEntrance
import tech.pixelw.flick.theme.FlickTheme

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { _, insets -> insets }
        setContent {
            FlickTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                    val drawerOpen by viewModel.drawerOpened.collectAsState()
                    val currentTitleModel by viewModel.currentTitleModel.collectAsState()
                    viewModel.getPreferScreen(this@MainActivity)
                    if (drawerOpen) {
                        LaunchedEffect(Unit) {
                            // Open drawer and reset state in VM.
                            try {
                                drawerState.open()
                            } finally {
                                // wrap in try-finally to handle interruption whiles opening drawer
                                viewModel.resetOpenDrawerAction()
                            }
                        }
                    }

                    // Intercepts back navigation when the drawer is open
                    val scope = rememberCoroutineScope()
                    if (drawerState.isOpen) {
                        BackHandler {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    }
                    MainNavDrawer(drawerState = drawerState, screenContent = {
                        MainContentFrame(
                            title = currentTitleModel.title,
                            upperTitle = currentTitleModel.upperTitle,
                            lowerTitle = currentTitleModel.lowerTitle
                        ) {
                            AndroidViewBinding(ContentMainBinding::inflate)
                        }
                    }, onItemClicked = {
                        when (it) {
                            MainNavDrawerEntrance.BD_STATION -> {
                                findNavController(R.id.nav_host_fragment).navigate(R.id.screen_station)
                            }

                            MainNavDrawerEntrance.BD_EVENTS -> todo()
                            MainNavDrawerEntrance.BD_SONGS -> todo()
                            MainNavDrawerEntrance.PJSK_STATION -> todo()
                            MainNavDrawerEntrance.PJSK_EVENTS -> todo()
                            MainNavDrawerEntrance.PJSK_SONGS -> todo()
                            MainNavDrawerEntrance.APP_MUSIC_PLAYER -> {
                                findNavController(R.id.nav_host_fragment).navigate(R.id.screen_music_player)
                            }

                            MainNavDrawerEntrance.APP_SETTINGS -> todo()
                        }

                    })
                }
            }
        }
    }

    private fun todo() {
        toast("Not implemented yet...")
    }
}