package tech.pixelw.flick.feature.station

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
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
import tech.pixelw.flick.feature.station.bandori.data.BsMessage
import tech.pixelw.flick.feature.station.bandori.data.BsRoom
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

private val OpponentChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val SelfChatBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)

@Composable
fun MessageCell(isUserMe: Boolean, entry: BsBaseEntity, onAvatarClick: ((BsUserInfo) -> Unit)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!isUserMe) {
            UserAvatar(entry.userInfo, onAvatarClick, false)
        }
        val alignment = if (isUserMe) {
            Alignment.End
        } else {
            Alignment.Start
        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .padding(horizontal = 4.dp),
            horizontalAlignment = alignment
        ) {
            AuthorTimestamp(entry.userInfo, entry)

            val backgroundColor = if (isUserMe) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }

            val shape = if (isUserMe) SelfChatBubbleShape else OpponentChatBubbleShape
            Column {
                when (entry) {
                    is BsMessage -> {

                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(shape = shape, color = backgroundColor) {
                            Text(
                                text = entry.content, modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current)
                            )
                        }

                    }

                    is BsRoom -> {

                    }
                }
            }


        }
        if (isUserMe) {
            UserAvatar(entry.userInfo, onAvatarClick, true)
        }

    }
}

@Composable
private fun UserAvatar(
    userInfo: BsUserInfo,
    onAvatarClick: (BsUserInfo) -> Unit,
    isUserMe: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }
    AsyncImage(model = userInfo.getAvatarUrl(),
        contentDescription = stringResource(R.string.someones_avatar, userInfo.username),
        modifier = Modifier
            .clickable { onAvatarClick(userInfo) }
            .padding(end = 14.dp)
            .size(42.dp)
            .border(1.5.dp, borderColor, CircleShape)
            .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}


@Composable
private fun AuthorTimestamp(
    userInfo: BsUserInfo,
    entry: BsBaseEntity
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = userInfo.username, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = SimpleDateFormat.getDateInstance().format(entry.timestamp),
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
fun StationRoomCellPreview() {
    FlickTheme {
        Surface {
            MessageCell(isUserMe = true, BsMessage("I'm new in jetpack compose a").apply {
                userInfo = BsUserInfo(username = "Pixelw")
            }) {

            }
        }
    }
}