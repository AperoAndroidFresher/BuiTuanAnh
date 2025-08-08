package com.example.buituananh.presentation.navigation

import android.content.ContentResolver
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.buituananh.di.AppContainer
import com.example.buituananh.presentation.home.HomeScreen
import com.example.buituananh.presentation.library.LibraryViewModel
import com.example.buituananh.presentation.library.component.LibraryScreenRoot
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.presentation.playlist.PlaylistViewModel
import com.example.buituananh.presentation.profile.ProfileViewModel
import com.example.buituananh.presentation.profile.component.ProfileScreenRoot
import com.example.buituananh.util.Destination

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    contentResolver: ContentResolver,
    appContainer: AppContainer
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

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Column { 
                MiniPlayer(appContainer.playerViewModel)
                if (currentScreen is Destination.HomeScreen
                    || currentScreen is Destination.LibraryScreen
                    || currentScreen is Destination.PlaylistWrapper
                ) {
                    BottomBar(
                        currentDestination = currentDestinationIdx,
                        onDestinationChange = {
                            currentDestinationIdx = it
                        }
                    ) { route ->
                        backStack.removeLastOrNull()
                        backStack.add(route)
                    }
                }
            }
        }
    ) { pd ->
        NavDisplay(
            modifier = modifier.padding(pd),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Destination.AuthWrapper> { parentKey ->
                    val loginViewModel = viewModel<LoginViewModel>(factory = LoginViewModel.Factory(parentKey, appContainer.userRepository))
                    AuthWrapperEntry(
                        addToBackStack = {
                            backStack.add(it)
                        },
                        onBack = {
                            backStack.removeLastOrNull()
                        },
                        loginViewModel = loginViewModel,
                        appContainer = appContainer,
                        backStack = backStack
                    )
                }
                entry<Destination.HomeScreen> {
                    HomeScreen {
                        backStack.add(it)
                    }
                }
                entry<Destination.LibraryScreen> { key ->
                    LibraryScreenRoot(
                        viewModel = viewModel(
                            factory = LibraryViewModel.Factory(
                                key,
                                contentResolver,
                                appContainer.userRepository,
                                appContainer.playlistRepository,
                                appContainer.songRepository,
                                appContainer.fetchAndCacheSongsUseCase,
                                appContainer.playerViewModel
                            )
                        )
                    ) {
                        backStack.add(it)
                    }
                }
                entry<Destination.PlaylistWrapper> { key ->
                    val viewModel = viewModel<PlaylistViewModel>(
                        factory = PlaylistViewModel.Factory(
                            key,
                            appContainer.userRepository,
                            appContainer.playlistRepository
                        )
                    )
                    PlaylistWrapperEntry(viewModel)
                }
                entry<Destination.ProfileScreen> { key ->
                    ProfileScreenRoot(
                        viewModel = viewModel(
                            factory = ProfileViewModel.Factory(
                                key,
                                appContainer.userRepository
                            )
                        ),
                    )
                }
            }
        )
    }

}
