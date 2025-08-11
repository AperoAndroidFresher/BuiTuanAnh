package com.example.buituananh.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.buituananh.R
import com.example.buituananh.presentation.home.HomeScreen
import com.example.buituananh.presentation.library.LibraryViewModel
import com.example.buituananh.presentation.library.component.LibraryScreenRoot
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.presentation.player.PlayerIntent
import com.example.buituananh.presentation.player.PlayerViewModel
import com.example.buituananh.presentation.player.components.MiniPlayerBar
import com.example.buituananh.presentation.player.components.PlayerScreenRoot
import com.example.buituananh.presentation.playlist.PlaylistViewModel
import com.example.buituananh.presentation.profile.ProfileViewModel
import com.example.buituananh.presentation.profile.component.ProfileScreenRoot
import com.example.buituananh.util.Destination

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {

    val backStack = rememberNavBackStack(Destination.AuthWrapper)

    var currentDestinationIdx by remember {
        mutableIntStateOf(0)
    }

    val currentScreen by remember {
        derivedStateOf {
            backStack.last()
        }
    }

    val playerViewModel = hiltViewModel<PlayerViewModel>()
    val musicState = playerViewModel.state.collectAsStateWithLifecycle().value

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentScreen is Destination.HomeScreen
                || currentScreen is Destination.LibraryScreen
                || currentScreen is Destination.PlaylistWrapper
            ) {
                val isCancel = musicState.musicState?.isCancel ?: false
                Column {
                    AnimatedVisibility(!isCancel) {
                        MiniPlayerBar(
                            navigateToPlayerScreen = {
                                backStack.add(Destination.PlayerWrapper)
                            },
                            togglePlayPause = {
                                playerViewModel.onIntent(PlayerIntent.TogglePlayPauseMode)
                            },
                            isPlaying = musicState.musicState?.isPlaying ?: false,
                            currentProgress = musicState.musicState?.progress ?: 0L,
                            title = musicState.musicState?.currentSong?.title ?: "No title",
                            duration = musicState.musicState?.currentSong?.duration?.toMilliseconds() ?: 0L,
                        )
                    }
                    BottomBar(
                        currentDestination = currentDestinationIdx,
                        onDestinationChange = {
                            currentDestinationIdx = it
                        },
                    ) { route ->
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }
            }
        },
        floatingActionButton = {
            if(musicState.musicState?.isPlaying == true) {
                Icon(
                    painter = painterResource(R.drawable.cancel),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable {
                            playerViewModel.onIntent(PlayerIntent.StopPlaying)
                        },
                )
            }
        },
    ) { pd ->
        NavDisplay(
            modifier = modifier.padding(pd),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                entry<Destination.AuthWrapper> { parentKey ->
                    val loginViewModel = hiltViewModel<LoginViewModel, LoginViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(parentKey)
                        },
                    )
                    AuthWrapperEntry(
                        addToBackStack = {
                            backStack.add(it)
                        },
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        loginViewModel = loginViewModel,
                        backStack = backStack,
                    )
                }
                entry<Destination.HomeScreen> {
                    HomeScreen {
                        backStack.add(it)
                    }
                }
                entry<Destination.LibraryScreen> { key ->
                    val viewModel = hiltViewModel<LibraryViewModel, LibraryViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        },
                    )
                    LibraryScreenRoot(
                        viewModel = viewModel,
                    ) {
                        backStack.add(it)
                    }
                }
                entry<Destination.PlaylistWrapper> { key ->
                    val viewModel = hiltViewModel<PlaylistViewModel, PlaylistViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        },
                    )
                    PlaylistWrapperEntry(viewModel)
                }
                entry<Destination.ProfileScreen> { key ->
                    val viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        },
                    )
                    ProfileScreenRoot(
                        viewModel = viewModel,
                    )
                }
                entry<Destination.PlayerWrapper> {
                    PlayerScreenRoot(
                        viewModel = playerViewModel,
                        popBack = {
                            backStack.removeLastOrNull()
                        },
                    )
                }
            },
        )
    }

}
