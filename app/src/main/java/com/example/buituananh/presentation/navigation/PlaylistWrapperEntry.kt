package com.example.buituananh.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.buituananh.presentation.playlist.PlaylistViewModel
import com.example.buituananh.presentation.playlist.detail_playlist_component.DetailPlaylistScreenRoot
import com.example.buituananh.presentation.playlist.playlist_component.PlaylistScreenRoot
import com.example.buituananh.util.Destination

@Composable
fun PlaylistWrapperEntry(
    viewModel: PlaylistViewModel,
    playlistBackStack: NavBackStack,
    onBack: () -> Unit,
    addDestination: (Destination) -> Unit
) {
    
    NavDisplay(
        backStack = playlistBackStack,
        onBack = {
            onBack()
        },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Destination.PlaylistScreen> {
                PlaylistScreenRoot(viewModel = viewModel) {
                    addDestination(it)
                }
            }
            entry<Destination.DetailPlaylistScreen> {
                DetailPlaylistScreenRoot(
                    id = it.id,
                    viewModel = viewModel
                )
            }
        }
    )
}
