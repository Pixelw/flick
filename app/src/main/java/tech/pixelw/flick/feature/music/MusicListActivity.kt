package tech.pixelw.flick.feature.music

import android.content.ComponentName
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
import androidx.compose.runtime.snapshotFlow
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
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import coil.compose.AsyncImage
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import tech.pixelw.flick.R
import tech.pixelw.flick.core.ui.theme.FlickTheme
import tech.pixelw.flick.feature.music.data.MusicModel

class MusicListActivity : ComponentActivity() {

    private val viewModel by viewModels<MusicListViewModel>()

    private var player: Player? = null

    private lateinit var controllerFuture: ListenableFuture<MediaController>
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
                            SwipeMusicBar(
                                list = musicList!!,
                                currentIndex = currentIndex,
                                onPageChanged = { player?.seekToDefaultPosition(it) },
                                onClick = {
                                    startActivity(Intent(this@MusicListActivity, MusicPlayActivity::class.java))
                                })
                        }

                    }

                }
            }
        }
        viewModel.loadData()
    }

    override fun onStart() {
        super.onStart()
        // 链接到播放服务
        val token = SessionToken(this, ComponentName(this, MusicPlayService::class.java))
        controllerFuture = MediaController.Builder(this, token).buildAsync()
        controllerFuture.addListener({
            // 服务已连接后
            player = controllerFuture.get()
        }, MoreExecutors.directExecutor())
    }

    override fun onStop() {
        super.onStop()
        player = null
        MediaController.releaseFuture(controllerFuture)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeMusicBar(list: List<MusicModel>, currentIndex: Int, onClick: () -> Unit, onPageChanged: (Int) -> Unit) {
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
            snapshotFlow { state.settledPage }.flowOn(Dispatchers.Main).collect { page ->
                if (MusicPlaylistHelper.playIndex == page) return@collect
                onPageChanged(page)
            }
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