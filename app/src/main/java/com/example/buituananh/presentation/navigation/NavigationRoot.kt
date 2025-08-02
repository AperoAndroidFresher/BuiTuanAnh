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
import com.example.buituananh.presentation.home.HomeScreen
import com.example.buituananh.presentation.library.LibraryScreenRoot
import com.example.buituananh.presentation.library.LibraryViewModel
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.presentation.login.LoginScreenRoot
import com.example.buituananh.presentation.playlist.DetailPlaylistScreenRoot
import com.example.buituananh.presentation.playlist.PlaylistScreenRoot
import com.example.buituananh.presentation.playlist.PlaylistViewModel
import com.example.buituananh.presentation.profile.ProfileScreenRoot
import com.example.buituananh.presentation.profile.ProfileViewModel
import com.example.buituananh.presentation.signup.SignupScreenRoot
import com.example.buituananh.presentation.signup.SignupViewModel
import com.example.buituananh.presentation.splash.SplashScreen
import com.example.buituananh.util.Destination

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    contentResolver: ContentResolver
) {

    val backStack = rememberNavBackStack(Destination.HomeScreen)
    val backStack2 = rememberNavBackStack(Destination.PlaylistScreen)

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
                || currentScreen is Destination.PlaylistWrapper) {
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
                entry<Destination.SplashScreen> {
                    SplashScreen {
                        backStack.add(Destination.LoginScreen)
                    }
                }
                entry<Destination.LoginScreen> { key: Destination.LoginScreen ->
                    LoginScreenRoot(
                        viewModel = viewModel(factory = LoginViewModel.Factory(key))
                    ) { route ->
                        if(route is Destination.HomeScreen) {
                            while(backStack.isNotEmpty()) {
                                backStack.removeLastOrNull()
                            }
                        }
                        backStack.add(route)
                    }
                }
                entry<Destination.SignupScreen> { key: Destination.SignupScreen ->
                    SignupScreenRoot(
                        viewModel = viewModel(factory = SignupViewModel.Factory(System.currentTimeMillis())),
                        onPopBack = {
                            backStack.removeLastOrNull()
                        }
                    ) {
                        backStack.add(it)
                    }
                }
                entry<Destination.HomeScreen> {
                    HomeScreen {
                        backStack.add(it)
                    }
                }
                entry<Destination.LibraryScreen> { key ->
                    LibraryScreenRoot(
                        viewModel = viewModel(factory = LibraryViewModel.Factory(key, contentResolver))
                    ) {
                        backStack.add(it)
                    }
                }
                entry<Destination.PlaylistWrapper> { key ->
                    val viewModel = viewModel<PlaylistViewModel>(factory = PlaylistViewModel.Factory(key))
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
                        viewModel = viewModel(factory = ProfileViewModel.Factory(key)),
                    )
                }
            }
        )
    }

}