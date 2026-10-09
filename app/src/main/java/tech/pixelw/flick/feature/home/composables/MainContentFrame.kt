package tech.pixelw.flick.feature.home.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import tech.pixelw.flick.R
import tech.pixelw.flick.theme.FlickTheme

/** 显示首页工具栏，并将系统栏安全间距应用到嵌入的页面内容。 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainContentFrame(title: String, upperTitle: String = "", lowerTitle: String = "", content: @Composable () -> Unit, onNavIconPressed: () -> Unit) {
    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            MainContentAppBar(upperTitle = upperTitle, title = title, lowerTitle = lowerTitle, onNavIconPressed = onNavIconPressed)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            content()
        }
    }
}

/** 显示导航按钮和三级标题，并避开状态栏及屏幕刘海。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentAppBar(title: String, upperTitle: String = "", lowerTitle: String = "", onNavIconPressed: () -> Unit = { }) {

    @Composable
    fun titleStack(upperTitle: String, title: String, lowerTitle: String) {
        Column {
            if (upperTitle.isNotBlank()) {
                Text(text = upperTitle, fontSize = 9.sp, letterSpacing = 2.sp, color = MaterialTheme.colorScheme.outline)
            }
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            if (lowerTitle.isNotBlank()) {
                Text(text = lowerTitle, fontSize = 9.sp, letterSpacing = 1.sp, color = MaterialTheme.colorScheme.outline)
            }
        }
    }

    TopAppBar(
        windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
        title = { titleStack(upperTitle = upperTitle, title = title, lowerTitle = lowerTitle) },
        navigationIcon = {
            IconButton(onClick = { onNavIconPressed() }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_arrow_back_24),
                    contentDescription = "Back Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Preview
@Composable
fun MainContentFramePreview() {
    FlickTheme {
        MainContentFrame(title = "Preview Title", content = {

        }, onNavIconPressed = {})
    }

}
