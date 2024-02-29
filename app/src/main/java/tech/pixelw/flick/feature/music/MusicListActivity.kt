package tech.pixelw.flick.feature.music

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.ui.theme.FlickTheme
import tech.pixelw.flick.feature.music.data.MusicModel

class MusicListActivity : ComponentActivity() {

    private val viewModel by viewModels<MusicListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Column(Modifier.fillMaxSize()) {
                        val musicList by viewModel.dataList.observeAsState()
                        val currentIndex by viewModel.currentPlayIndex.observeAsState(0)
                        if (!musicList.isNullOrEmpty()) {
                            MusicList(musicList!!, onClick = {
                                viewModel.preparePlaylist()
                                startActivity(Intent(this@MusicListActivity, MusicPlayActivity::class.java).apply {
                                    putExtra(MusicPlayActivity.K_MUSIC_ID, it.mediaId)
                                })
                            }, modifier = Modifier.weight(1f))
                            SwipeMusicBar(list = musicList!!, currentIndex = currentIndex) {
                                startActivity(Intent(this@MusicListActivity, MusicPlayActivity::class.java))
                            }
                        }

                    }

                }
            }
        }
        viewModel.loadData()
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeMusicBar(list: List<MusicModel>, currentIndex: Int, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(6.dp)
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.background,
        onClick = onClick
    ) {
        val state = rememberPagerState(pageCount = { list.size })
        HorizontalPager(state = state) { index ->
            val model = list[index]
            MusicPlayingBar(title = model.musicTitle ?: "???", artist = model.getBandName(), imgUrl = model.getSongArtUrl())
        }
        val rememberCoroutineScope = rememberCoroutineScope()
        LaunchedEffect(currentIndex) {
            rememberCoroutineScope.launch {
                state.scrollToPage(currentIndex)
            }
//            snapshotFlow { state.settledPage }.collect { page ->
//
//            }
        }
    }
}


@Preview
@Composable
fun PreviewMusicPlayingBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), color = MaterialTheme.colorScheme.background
    ) {
        MusicPlayingBar(
            title = "YES! BanG_Dream!!!!!!!!!!!!!",
            artist = "Poppin'Party!!!!!!!!!!!!",
            imgUrl = "https://lapi.pixelw.tech/bandroid/img/musicjacket/177_jacket1.webp"
        )
    }
}

@Composable
fun MusicPlayingBar(title: String, artist: String, imgUrl: String) {


    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            modifier = Modifier.size(40.dp),
            model = imgUrl,
            contentDescription = stringResource(id = R.string.music_art)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            buildAnnotatedString {
                append(title)
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append(" - $artist")
                }
            }, style = MaterialTheme.typography.titleSmall, maxLines = 2
        )

    }
}

@Composable
fun MusicList(state: List<MusicModel>, onClick: (MusicModel) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(state) {
            MusicListCell(it, onClick)
        }
    }
}