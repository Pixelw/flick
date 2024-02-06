package tech.pixelw.flick.music

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

class MusicListActivity : ComponentActivity() {

    val viewModel by viewModels<MusicListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MusicList(viewModel = viewModel)
                }
            }
        }
        viewModel.loadData()
    }
}

@Composable
fun MusicList(viewModel: MusicListViewModel) {
    val s by viewModel.dataList.observeAsState()
    LazyColumn {
        if (s == null) return@LazyColumn
        items(s!!) {
            MusicListCell(it)
        }
    }
}