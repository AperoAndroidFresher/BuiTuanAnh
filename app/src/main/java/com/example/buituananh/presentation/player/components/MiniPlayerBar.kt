package com.example.buituananh.presentation.player.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.buituananh.R
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.toMinuteSecondString

@Composable
fun MiniPlayerBar(
    navigateToPlayerScreen: () -> Unit,
    togglePlayPause: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false,
    currentProgress: Long = 0L,
    duration: Long = 0L,
    title: String = "",
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column {
            ProgressBar(
                currentProgress = currentProgress,
                duration = duration,
                modifier = Modifier,
            )
            StatusBar(
                togglePlayPause = togglePlayPause,
                title = title,
                currentProgress = currentProgress,
                isPlaying = isPlaying,
                modifier = Modifier
                    .padding(0.dp)
                    .padding(horizontal = 12.dp)
                    .clickable { navigateToPlayerScreen() },
            )
        }
    }
}

@Composable
fun StatusBar(
    togglePlayPause: () -> Unit,
    modifier: Modifier = Modifier,
    title: String = "",
    currentProgress: Long = 0L,
    isPlaying: Boolean = false,
) {

    val playPauseIcon = if (isPlaying) {
        R.drawable.pause
    } else {
        R.drawable.play
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = togglePlayPause,
        ) {
            Icon(
                painter = painterResource(playPauseIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(Modifier.width(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(20.dp))
        Text(
            text = currentProgress.toMinuteSecondString(),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    currentProgress: Long = 0L,
    duration: Long = 0L,
) {

    val density = LocalDensity.current
    val screenWidthPx = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    val currentWidthDp by animateDpAsState(
        targetValue = with(density) {
            ((currentProgress.toFloat() / duration) * screenWidthPx).toDp()
        },
        label = "progressWidth",
    )

    Box(
        modifier = modifier
            .height(8.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceBright),
    ) {
        Box(
            modifier = Modifier
                .height(8.dp)
                .width(currentWidthDp)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.CenterStart),
        )
    }
}

@Preview
@Composable
private fun PreviewMiniPlayerBar() {
    BuiTuanAnhTheme {
        MiniPlayerBar(
            togglePlayPause = {},
            currentProgress = 100L,
            duration = 1000L,
            title = "Anh khong lam gi dau anh the",
            isPlaying = true,
            navigateToPlayerScreen = {}
        )
    }
}

@Preview(
    name = "StatusBar Playing",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
private fun PreviewStatusBarPlaying() {
    MaterialTheme {
        StatusBar(
            togglePlayPause = {},
            title = "Song Title Example",
            currentProgress = 125_000L,
            isPlaying = true,
        )
    }
}
