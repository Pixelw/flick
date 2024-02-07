package tech.pixelw.flick.music

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tech.pixelw.flick.R
import tech.pixelw.flick.common.resources.ResourceMapper
import tech.pixelw.flick.music.data.MusicModel

@Composable
@Preview
fun PreviewMusicListCell() {
    MusicListCell(
        MusicModel(
            musicTitle = "Tell Your World",
            bandName = "Hatsune Miku",
            length = 125.4,
            composer = "kz (livetune)",
            jacketImage = listOf("https://brands.home-assistant.io/_/jellyfin/logo.png"),
            musicSerialId = 15
        )
    )
}

@Composable
fun MusicListCell(model: MusicModel) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box {
                Text(
                    text = model.musicSerialId.toString(), style = MaterialTheme.typography.titleMedium, modifier = Modifier
                        .size(50.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically), textAlign = TextAlign.Center
                )
                AsyncImage(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = model.getAlbumArtUrl(1), contentDescription = stringResource(R.string.music_art)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(Modifier.weight(1.0f, fill = true)) {
                Text(style = MaterialTheme.typography.titleMedium, text = model.musicTitle ?: "")
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (model.bandName.isNullOrEmpty()) {
                        ResourceMapper.getBandNameString(model.bandId)
                    } else model.bandName, style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = DateUtils.formatElapsedTime(model.length.toLong()), style = MaterialTheme.typography.titleSmall)
        }
    }

}