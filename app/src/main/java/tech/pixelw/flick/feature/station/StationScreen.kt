package tech.pixelw.flick.feature.station

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
fun StationScreen(list: List<BsBaseEntity>, myUid: String) {
    LazyColumn {
        items(list) {
            StationCell(isUserMe = it.isMySide(myUid), entry = it) { type, entity ->
                // TODO: SLY 24/7/3 click
            }
        }
    }
}

private val OpponentChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
private val SelfChatBubbleShape = RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
private val RoomBubbleShape = RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp)

enum class StationCellClickType {
    ROOM,
    AVATAR,
    LINK
}

@Composable
fun StationCell(isUserMe: Boolean, entry: BsBaseEntity, onClick: ((StationCellClickType, BsBaseEntity) -> Unit)) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (!isUserMe) {
            UserAvatar(entry.userInfo, {
                onClick(StationCellClickType.AVATAR, entry)
            }, false)
        }
        val alignment = if (isUserMe) {
            Alignment.End
        } else {
            Alignment.Start
        }
        Column(
            modifier = Modifier
                .weight(1.0f)
                .padding(horizontal = 8.dp),
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
                Spacer(modifier = Modifier.height(4.dp))
                when (entry) {
                    is BsMessage -> {
                        ChatCard(shape, backgroundColor, entry)
                    }

                    is BsRoom -> {
                        RoomCard(shape, entry) {
                            onClick(StationCellClickType.ROOM, entry)
                        }
                    }
                }
            }


        }
        if (isUserMe) {
            UserAvatar(entry.userInfo, {
                onClick(StationCellClickType.AVATAR, entry)
            }, true)
        }

    }
}

@Composable
private fun ChatCard(
    shape: RoundedCornerShape,
    backgroundColor: Color,
    entry: BsMessage
) {
    Surface(shape = shape, color = backgroundColor) {
        Text(
            text = entry.content, modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current)
        )
    }
}

@Composable
private fun RoomCard(shape: RoundedCornerShape, entry: BsRoom, onClick: (() -> Unit)) {
    Surface(shape = shape, color = MaterialTheme.colorScheme.surfaceVariant) {
        Column(
            modifier = Modifier
                .defaultMinSize(minWidth = 200.dp, minHeight = 120.dp)
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
        ) {
            Box(
                contentAlignment = Alignment.Center, modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = entry.message,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onClick() }
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.round_directions_bus_filled_24),
                    contentDescription = stringResource(id = R.string.copy_and_jump),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(14.dp)
                )

                Spacer(Modifier.size(4.dp))

                Text(
                    text = entry.number,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(Modifier.size(4.dp))

                Text(
                    text = stringResource(R.string.copy_and_jump),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelMedium.copy(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )


                Spacer(Modifier.size(4.dp))

                Icon(
                    painter = painterResource(id = R.drawable.round_arrow_forward_ios_24),
                    contentDescription = stringResource(id = R.string.copy_and_jump),
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(10.dp)
                )

            }
        }

    }
}

@Composable
private fun UserAvatar(
    userInfo: BsUserInfo,
    onAvatarClick: () -> Unit,
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
            .clickable { onAvatarClick() }
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
        verticalAlignment = Alignment.Bottom
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
//            MessageCell(isUserMe = true, BsMessage("I'm new in jetpack compose a").apply {
//                userInfo = BsUserInfo(username = "Pixelw")
//            }) {
//
//            }
            StationCell(
                isUserMe = false,
                entry = BsRoom(
                    "114514",
                    "200w大分卡"
                ).apply {
                    userInfo = BsUserInfo(username = "Pixelw")
                    timestamp = 1719974730000
                }) { _, _ -> }
        }
    }
}

@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {}
) {
    var stationModeOn by rememberSaveable {
        mutableStateOf(false)
    }

    var textFieldState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    Surface(tonalElevation = 2.dp) {
        Column(modifier = modifier) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
//                TextField(
//                    onValueChange = {
//
//                    },
//                    value = textFieldState,
//                    onValueChange = {},
//                    placeholder = "Message..."
//                )
            }
        }
    }
}
