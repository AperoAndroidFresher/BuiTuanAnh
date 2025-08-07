package com.example.buituananh.presentation.navigation

import android.content.ContentResolver
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.buituananh.presentation.library.component.LibraryScreenRoot
import com.example.buituananh.presentation.library.LibraryViewModel
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.presentation.login.component.LoginScreenRoot
import com.example.buituananh.presentation.playlist.detail_playlist_component.DetailPlaylistScreenRoot
import com.example.buituananh.presentation.playlist.playlist_component.PlaylistScreenRoot
import com.example.buituananh.presentation.playlist.PlaylistViewModel
import com.example.buituananh.presentation.profile.component.ProfileScreenRoot
import com.example.buituananh.presentation.profile.ProfileViewModel
import com.example.buituananh.presentation.signup.component.SignupScreenRoot
import com.example.buituananh.presentation.signup.SignupViewModel
import com.example.buituananh.presentation.splash.SplashScreen
import com.example.buituananh.util.Destination

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    contentResolver: ContentResolver,
    appContainer: AppContainer
) {

    val backStack = rememberNavBackStack(Destination.AuthWrapper)
    val backStack2 = rememberNavBackStack(Destination.PlaylistScreen)
    val authBackstack = rememberNavBackStack(Destination.SplashScreen)
    
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
                    NavDisplay(
                        backStack = authBackstack,
                        onBack = { authBackstack.removeLastOrNull() },
                        entryDecorators = listOf(
                            rememberSceneSetupNavEntryDecorator(),
                            rememberSavedStateNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            entry<Destination.SplashScreen> {
                                SplashScreen(
                                    onNavigate = {
                                        if(it is Destination.LoginScreen) {
                                            authBackstack.add(Destination.LoginScreen)
                                        }
                                        if(it is Destination.HomeScreen) {
                                            backStack.add(Destination.HomeScreen)
                                        }
                                    },
                                    viewModel = loginViewModel
                                )
                            }
                            entry<Destination.LoginScreen> { key: Destination.LoginScreen ->
                                LoginScreenRoot(
                                    viewModel = loginViewModel
                                ) { route ->
                                    if (route is Destination.HomeScreen) {
                                        while (backStack.isNotEmpty()) {
                                            backStack.removeLastOrNull()
                                        }
                                        backStack.add(route)
                                    }
                                    if (route is Destination.SignupScreen) {
                                        authBackstack.add(route)
                                    }
                                }
                            }
                            entry<Destination.SignupScreen> { key: Destination.SignupScreen ->
                                SignupScreenRoot(
                                    viewModel = viewModel(
                                        factory = SignupViewModel.Factory(
                                            key,
                                            appContainer.userRepository
                                        )
                                    ),
                                    onPopBack = {
                                        authBackstack.removeLastOrNull()
                                    }
                                ) {
                                    authBackstack.add(it)
                                }
                            }
                        }
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
                                appContainer.fetchAndCacheSongsUseCase
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
                    NavDisplay(
                        backStack = backStack2,
                        onBack = {
                            backStack2.removeLastOrNull()
                        },
                        entryDecorators = listOf(
                            rememberSceneSetupNavEntryDecorator(),
                            rememberSavedStateNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        entryProvider = entryProvider {
                            entry<Destination.PlaylistScreen> {
                                PlaylistScreenRoot(viewModel = viewModel) {
                                    backStack2.add(it)
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
