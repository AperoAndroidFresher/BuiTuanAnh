package com.example.buituananh.presentation.navigation

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import androidx.room.coroutines.createFlow
import com.example.buituananh.R
import com.example.buituananh.presentation.home.HomeScreenRoot
import com.example.buituananh.presentation.home.HomeViewModel
import com.example.buituananh.presentation.home.components.HomeState
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
import com.example.buituananh.presentation.setting.SettingScreen
import com.example.buituananh.presentation.setting.SettingViewModel
import com.example.buituananh.service.PlayType
import com.example.buituananh.util.Destination
import com.example.buituananh.util.Utils
import com.example.buituananh.util.toMilliseconds

@Composable
fun NavigationRoot(
    newIntent: Intent?,
    modifier: Modifier = Modifier,
) {

    val backStack = rememberNavBackStack(Destination.AuthWrapper)

    var currentDestinationIdx by remember {
        mutableIntStateOf(0)
    }

    val currentScreen by remember {
        derivedStateOf { backStack.last() }
    }

    var firstNavGraphEntry by remember {
        mutableStateOf(false)
    }

    val playerViewModel = hiltViewModel<PlayerViewModel>()
    val musicState = playerViewModel.state.collectAsStateWithLifecycle().value

    LaunchedEffect(newIntent) {
        newIntent?.let { intent ->
            if (intent.action == Utils.OPEN_PLAYER) {
                backStack.add(Destination.AuthWrapper)
                backStack.add(Destination.PlayerWrapper)
                Log.d("NavigationRoot", "NavigationRoot: Open player screen")
            } else {
                Log.d("NavigationRoot", "NavigationRoot: Cannot receive")
            }
        }
    }
    
    LaunchedEffect(currentScreen) {
        if (currentScreen !is Destination.HomeWrapper
            && currentScreen !is Destination.LibraryScreen
            && currentScreen !is Destination.PlaylistWrapper
            && currentScreen !is Destination.PlayerWrapper
            && musicState.musicState?.currentSong != null
            && musicState.musicState.playType == PlayType.PREVIEW
        ) {
            playerViewModel.onIntent(PlayerIntent.StopPlaying)
        }
    }

    LaunchedEffect(Unit) {
        if (!firstNavGraphEntry) {
            if (musicState.musicState?.currentSong != null) {
                backStack.add(Destination.PlayerWrapper)
            }
        }
        firstNavGraphEntry = true
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (currentScreen is Destination.HomeWrapper
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
            if (musicState.musicState?.currentSong != null && (currentScreen is Destination.HomeWrapper || currentScreen is Destination.LibraryScreen || currentScreen is Destination.PlaylistWrapper)) {
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
                entry<Destination.HomeWrapper> { key ->
                    val homeViewModel = hiltViewModel<HomeViewModel, HomeViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )
                    HomeScreenRoot(
                        onNavigate = {
                            backStack.add(it)
                        },
                        viewModel = homeViewModel
                    )
                }
                entry<Destination.SettingScreen> { key ->
                    val settingViewModel = hiltViewModel<SettingViewModel, SettingViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )
                    SettingScreen(
                        viewModel = settingViewModel,
                        onNavigate = {
                            
                        },
                        popBack = {
                            backStack.removeLastOrNull()
                        }
                    )
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
