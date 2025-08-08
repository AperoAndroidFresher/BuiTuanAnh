package com.example.buituananh.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.R
import com.example.buituananh.domain.model.Song
import com.example.buituananh.presentation.player.*
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.SongSource
import com.example.buituananh.util.formatToString
import com.example.buituananh.util.toLongDuration

@Composable
fun MiniPlayer(
    playerViewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {

    val state = playerViewModel.musicState.collectAsStateWithLifecycle().value

    PlayerBar(
        state = state,
        onIntent = playerViewModel::onIntent,
    )
}

@Composable
fun PlayerBar(
    state: MusicState,
    onIntent: (PlayerIntent) -> Unit,
    modifier: Modifier = Modifier,
) {

    state.playerState?.let { player ->
        player.music?.let { selectedMusic ->
            Column(
                modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                Slider(
                    value = state.playerState.progress?.toFloat() ?: 0f,
                    onValueChange = {
                        onIntent(PlayerIntent.SliderChange(it, selectedMusic))
                    },
                    valueRange = 0f..(selectedMusic.duration?.toLongDuration() ?: 0L).toFloat(),
                    modifier = Modifier.height(3.dp),
                )
                SongBar(
                    playerAction = player.action,
                    title = player.music.title ?: "",
                    duration = player.music.duration ?: (0 to 0),
                    onSelectedAudio = { action ->
                        if (action == PlayerAction.PAUSE) {
                            onIntent(PlayerIntent.ClickPlay)
                        } else {
                            onIntent(PlayerIntent.ClickPause)
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun SongBar(
    playerAction: PlayerAction,
    title: String,
    duration: Pair<Int, Int>,
    onSelectedAudio: (PlayerAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    val actionIcon = if (playerAction == PlayerAction.PLAY || playerAction == PlayerAction.START) {
        R.drawable.pause
    } else {
        R.drawable.play
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = {
                onSelectedAudio(playerAction)
            },
        ) {
            Icon(
                painter = painterResource(actionIcon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(25.dp),
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = duration.formatToString(),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun PreviewMiniPlayer() {
    BuiTuanAnhTheme {
        PlayerBar(
            state = MusicState(
                playerState = PlayerState(
                    id = 1,
                    music = Song(2, "ajodjawoidajaowidjoadwijd", "jdakwd", 3 to 5, null, null, SongSource.LOCAL),
                ),
            ),
            onIntent = {},
        )
    }
}
