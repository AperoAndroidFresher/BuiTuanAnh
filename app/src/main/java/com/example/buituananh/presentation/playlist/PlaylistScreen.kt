package com.example.buituananh.presentation.playlist

import android.graphics.Paint.Align
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.presentation.library.LibraryIntent
import com.example.buituananh.presentation.playlist.item.EmptyPlaylistNoti

@Composable
fun PlaylistScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value

    PlaylistScreen(
        state = state,
        onIntent = viewModel::onIntent
    )

}

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    state: PlaylistState,
    onIntent: (PlaylistIntent) -> Unit
) {

    LaunchedEffect(Unit) {
        onIntent(PlaylistIntent.LoadPlaylist)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.size(30.dp))

                Text(
                    text = "My Playlist",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.playlistList.isEmpty()) {
                item {
                    EmptyPlaylistNoti {

                    }
                }
            } else {

            }
        }
    }

}