package tech.pixelw.flick.music

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import tech.pixelw.flick.R
import tech.pixelw.flick.music.data.MusicModel

@Composable
@Preview
fun PreviewMusicListCell() {
    MusicListCell(
        MusicModel(
            musicTitle = "Tell Your World",
            bandName = "Hatsune Miku",
            composer = "kz (livetune)"

        )
    )
}

@Composable
fun MusicListCell(model: MusicModel) {
    Row {
        AsyncImage(
            model = model.jacketImage, contentDescription = stringResource(R.string.music_art), placeholder = painterResource(
                id = R.drawable.ic_launcher_background
            )
        )
    }
}