package tech.pixelw.flick.feature.home.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import tech.pixelw.flick.R
import tech.pixelw.flick.theme.FlickTheme

enum class MainNavDrawerEntrance(val group: Int, val stringId: Int) {
    BD_STATION(1, R.string.bandoristation),
    BD_EVENTS(1, R.string.events),
    BD_SONGS(1, R.string.songs),

    PJSK_STATION(2, R.string.pjskstation),
    PJSK_EVENTS(2, R.string.events),
    PJSK_SONGS(2, R.string.songs),

    APP_MUSIC_PLAYER(3, R.string.music_player),
    APP_SETTINGS(3, R.string.settings)
}

@Composable
fun MainNavDrawer(
    drawerState: DrawerState,
    screenContent: (@Composable () -> Unit),
    onItemClicked: ((MainNavDrawerEntrance) -> Unit),
    selected: MainNavDrawerEntrance
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                MainNavDrawerContent(onItemClicked, selected)
            }
        },
        content = screenContent,
        gesturesEnabled = false
    )
}

@Composable
fun MainNavDrawerContent(
    onItemClicked: ((MainNavDrawerEntrance) -> Unit),
    selectedIndex: MainNavDrawerEntrance
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader()
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

        DrawerGroupTitle(stringResource(id = R.string.bangdream_gbp))
        DrawerItem(
            iconRes = R.drawable.round_directions_bus_filled_24,
            text = stringResource(R.string.bandoristation),
            selected = selectedIndex == MainNavDrawerEntrance.BD_STATION
        ) {
            onItemClicked(MainNavDrawerEntrance.BD_STATION)
        }
        DrawerItem(
            iconRes = R.drawable.round_events_24,
            text = stringResource(R.string.events),
            selected = selectedIndex == MainNavDrawerEntrance.BD_EVENTS
        ) {
            onItemClicked(MainNavDrawerEntrance.BD_EVENTS)
        }
        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))

//        DrawerGroupTitle(title = stringResource(id = R.string.pj_sekai))
        DrawerGroupTitle(title = stringResource(id = R.string.tools))
        DrawerItem(
            iconRes = R.drawable.round_music_note_24,
            text = stringResource(R.string.music_player),
            selected = selectedIndex == MainNavDrawerEntrance.APP_MUSIC_PLAYER
        ) {
            onItemClicked(MainNavDrawerEntrance.APP_MUSIC_PLAYER)
        }

        Spacer(modifier = Modifier.weight(1f))
        DrawerItem(
            iconRes = R.drawable.round_settings_24,
            text = stringResource(id = R.string.settings),
            selected = selectedIndex == MainNavDrawerEntrance.APP_SETTINGS
        ) {
            onItemClicked(MainNavDrawerEntrance.APP_SETTINGS)
        }
        Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))

    }

}

@Composable
fun DrawerGroupTitle(title: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(start = 28.dp, end = 28.dp, top = 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun DrawerHeader() {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "App Icon",
            modifier = Modifier
                .clip(CircleShape)
                .size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Flick 1.0.0", style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
private fun DrawerItem(iconRes: Int, text: String, selected: Boolean, onClick: () -> Unit) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .then(background)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            painter = painterResource(id = iconRes),
            tint = iconTint,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            contentDescription = null
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
@Preview(wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE, uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
fun MainNavDrawerPreview() {
    FlickTheme {
        MainNavDrawer(rememberDrawerState(initialValue = DrawerValue.Open), screenContent = {}, onItemClicked = {}, MainNavDrawerEntrance.BD_STATION)
    }
}