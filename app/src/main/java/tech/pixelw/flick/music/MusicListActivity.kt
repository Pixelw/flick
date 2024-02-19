package tech.pixelw.flick.music

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import tech.pixelw.flick.common.ui.theme.FlickTheme
import tech.pixelw.flick.music.data.MusicModel

class MusicListActivity : ComponentActivity() {

    val viewModel by viewModels<MusicListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MusicList(viewModel = viewModel) {
                        startActivity(Intent(this, MusicPlayActivity::class.java))
                    }
                }
            }
        }
        viewModel.loadData()
    }
}

@Composable
fun MusicList(viewModel: MusicListViewModel, onClick: (MusicModel) -> Unit) {
    val state by viewModel.dataList.observeAsState()
    LazyColumn {
        if (state == null) return@LazyColumn
        items(state!!) {
            MusicListCell(it, onClick)
        }
    }
}