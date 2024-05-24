package tech.pixelw.flick.feature.station

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import tech.pixelw.flick.R
import tech.pixelw.flick.feature.station.bandori.data.BsBaseEntity
import tech.pixelw.flick.feature.station.bandori.data.BsUserInfo
import tech.pixelw.flick.theme.FlickTheme
import java.text.SimpleDateFormat

@Composable
fun StationScreen() {
    Surface {
        LazyColumn {

        }
    }
}


@Composable
fun StationCell(isUserMe: Boolean, userInfo: BsUserInfo, entry: BsBaseEntity, onAvatarClick: ((BsUserInfo) -> Unit)) {

    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!isUserMe) {
            AsyncImage(model = userInfo.getAvatarUrl(),
                contentDescription = stringResource(R.string.someones_avatar, userInfo.username),
                modifier = Modifier
                    .clickable { onAvatarClick(userInfo) }
                    .padding(end = 14.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                contentScale = ContentScale.Crop
            )

        } else {
            Spacer(modifier = Modifier.width(68.dp))
        }

        Column {
            Row {
                Text(text = userInfo.username, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = SimpleDateFormat.getDateInstance().format(entry.timestamp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

    }

}

@Preview
@Composable
fun StationRoomCellPreview() {
    FlickTheme {
        Surface {
            StationCell(isUserMe = false, BsUserInfo(username = "Pixel"), BsBaseEntity(), {})
        }
    }
}