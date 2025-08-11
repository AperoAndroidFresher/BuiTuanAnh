package com.example.buituananh.presentation.player.components

import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.presentation.components.TopBarTwoActions
import com.example.buituananh.presentation.player.PlayerEffect
import com.example.buituananh.presentation.player.PlayerIntent
import com.example.buituananh.presentation.player.PlayerState
import com.example.buituananh.presentation.player.PlayerViewModel
import com.example.buituananh.service.MusicState
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.*

@Composable
fun PlayerScreenRoot(
    viewModel: PlayerViewModel,
    popBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PlayerEffect.NavigateToHomeScreen -> popBack()
            }
        }
    }

    PlayerScreen(
        state = state,
        onIntent = viewModel::onIntent,
    )
}

@Composable
fun PlayerScreen(
    state: PlayerState,
    onIntent: (PlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidthDp - 320.dp) / 2

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBarTwoActions(
                onBack = {
                    onIntent(PlayerIntent.ClickBack)
                },
                onAction = {
                    onIntent(PlayerIntent.StopPlaying)
                },
                title = "Now Playing",
            )
        },
    ) { paddingValues ->
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))
            ImageSong(uri = state.musicState?.currentSong?.imageUri)
            Spacer(Modifier.height(12.dp))
            TitleSection(
                title = state.musicState?.currentSong?.title ?: "",
                artist = state.musicState?.currentSong?.artist ?: "",
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )
            Spacer(Modifier.height(12.dp))
            SliderSection(
                onValueChange = {
                    onIntent(PlayerIntent.DragSlider(it))
                },
                onDragEnd = {
                    onIntent(PlayerIntent.DragSliderEnd)
                },
                currentProgress = state.musicState?.progress,
                duration = state.musicState?.currentSong?.duration ?: (0 to 0),
                modifier = Modifier.padding(horizontal = horizontalPadding),
            )
            PlayerControl(
                toggleRepeatMode = { onIntent(PlayerIntent.ToggleRepeatMode) },
                toggleShuffleMode = { onIntent(PlayerIntent.ToggleShuffleMode) },
                togglePlayPause = { onIntent(PlayerIntent.TogglePlayPauseMode) },
                playNextSong = { onIntent(PlayerIntent.ClickNextSong) },
                playPrevSong = { onIntent(PlayerIntent.ClickPreviousSong) },
                isShuffleMode = state.musicState?.isShuffleMode ?: false,
                isPlaying = state.musicState?.isPlaying ?: false,
                isRepeatMode = state.musicState?.isRepeatMode ?: false,
                modifier = Modifier.padding(horizontalPadding)
            )
        }
    }
}

@Composable
fun PlayerControl(
    toggleShuffleMode: () -> Unit,
    toggleRepeatMode: () -> Unit,
    togglePlayPause: () -> Unit,
    playPrevSong: () -> Unit,
    playNextSong: () -> Unit,
    modifier: Modifier = Modifier,
    isShuffleMode: Boolean = false,
    isRepeatMode: Boolean = false,
    isPlaying: Boolean = false
) {

    val turnOnShuffleColor = animateColorAsState(
        targetValue = if (isShuffleMode) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.surfaceBright
        },
    )

    val turnOnRepeatColor = animateColorAsState(
        targetValue = if (isRepeatMode) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.surfaceBright
        },
    )
    
    val playPauseIcon = if(isPlaying) {
        R.drawable.pause
    } else {
        R.drawable.play
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IconButton(
            onClick = toggleShuffleMode,
        ) {
            Icon(
                painter = painterResource(R.drawable.shuffle),
                contentDescription = null,
                tint = turnOnShuffleColor.value,
                modifier = Modifier.size(20.dp),
            )
        }

        IconButton(
            onClick = playPrevSong,
        ) {
            Icon(
                painter = painterResource(R.drawable.prev),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp),
            )
        }

        Box(
            modifier = Modifier
                .size(60.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .clickable {
                    togglePlayPause()
                },
        ) {
            Icon(
                painter = painterResource(playPauseIcon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(38.dp)
                    .padding(8.dp)
                    .align(Alignment.Center),
            )
        }

        IconButton(
            onClick = playNextSong,
        ) {
            Icon(
                painter = painterResource(R.drawable.next),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp),
            )
        }

        IconButton(
            onClick = toggleRepeatMode,
        ) {
            Icon(
                painter = painterResource(R.drawable.repeat),
                contentDescription = null,
                tint = turnOnRepeatColor.value,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderSection(
    onValueChange: (Float) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier,
    currentProgress: Long? = 0L,
    duration: Pair<Int, Int> = 0 to 0,
) {
    val totalDurationMs = duration.toMilliseconds().toFloat().coerceAtLeast(1f)
    val progressValue = currentProgress?.toFloat() ?: 0f
    val fraction = (progressValue / totalDurationMs).coerceIn(0f, 1f)
    val density = LocalDensity.current
    val thumbRadiusPx = density.run { 6.dp.toPx() }
    val trackWidthPx = remember { mutableFloatStateOf(0f) }
    
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Slider(
            value = progressValue,
            onValueChange = onValueChange,
            onValueChangeFinished = onDragEnd,
            valueRange = 0f..totalDurationMs,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent,
                thumbColor = Color(0xFF7DF0FF)
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(Color(0xFF7DF0FF), shape = CircleShape)
                )
            },
            track = {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF7A7F82))
                        .onSizeChanged { size ->
                            trackWidthPx.floatValue = size.width.toFloat()
                        }
                ) {
                    val extraFraction = if (trackWidthPx.floatValue > 0f) {
                        thumbRadiusPx / trackWidthPx.floatValue
                    } else 0f
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((fraction + extraFraction).coerceIn(0f, 1f))
                            .background(Color(0xFF7DF0FF))
                    )
                }
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = currentProgress?.toMinuteSecondString() ?: "00:00",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = duration.formatToString(),
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
fun TitleSection(
    modifier: Modifier = Modifier,
    title: String = "",
    artist: String = "",
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = artist,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
        )
    }
}

@Composable
fun ImageSong(
    modifier: Modifier = Modifier,
    uri: Uri? = null,
) {

    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(uri)
            .size(300)
            .crossfade(true)
            .error(R.drawable.default_song)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .size(320.dp),
    )
}

@Preview
@Composable
private fun PreviewPlayerScreen() {
    BuiTuanAnhTheme {
        val song = Song(
            songId = 0,
            title = "grainy days",
            artist = "moody.",
            duration = 3 to 40,
            filePath = null,
            imageUri = null,
            songSource = SongSource.LOCAL,
        )
        PlayerScreen(
            state = PlayerState(MusicState(currentSong = song)),
            onIntent = {},
        )
    }
}
