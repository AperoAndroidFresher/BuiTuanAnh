package com.example.buituananh.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.buituananh.presentation.navigation.bottom_bar.BottomBar
import com.example.buituananh.presentation.home.HomeScreen
import com.example.buituananh.presentation.library.LibraryScreen
import com.example.buituananh.presentation.login.screen.LoginScreen
import com.example.buituananh.presentation.playlist.PlaylistScreen
import com.example.buituananh.presentation.profile.screen.ProfileScreen
import com.example.buituananh.presentation.signup.screen.SignupScreen
import com.example.buituananh.presentation.splash.SplashScreen
import com.example.buituananh.util.Destination

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeChange: () -> Unit
) {

    val backStack = rememberNavBackStack(Destination.SplashScreen)

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
                || currentScreen is Destination.PlaylistScreen) {
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
                rememberSavedStateNavEntryDecorator(),
                rememberSceneSetupNavEntryDecorator()
            ),
            entryProvider = { key ->
                when (key) {

                    is Destination.SplashScreen -> {
                        NavEntry(key) {
                            SplashScreen {
                                backStack.removeLastOrNull()
                                backStack.add(Destination.LoginScreen)
                            }
                        }
                    }

                    is Destination.LoginScreen -> {
                        NavEntry(key) {
                            LoginScreen { route ->
                                if(route is Destination.HomeScreen) {
                                    while(backStack.isNotEmpty()) {
                                        backStack.removeLastOrNull()
                                    }
                                }
                                backStack.add(route) //home
                            }
                        }
                    }

                    is Destination.SignupScreen -> {
                        NavEntry(key) {
                            SignupScreen(
                                onPopBack = {
                                    backStack.removeLastOrNull()
                                }
                            ) { route ->
                                backStack.add(route)
                            }
                        }
                    }

                    is Destination.HomeScreen -> {
                        NavEntry(key) {
                            HomeScreen { route ->
                                backStack.add(route)
                            }
                        }
                    }

                    is Destination.LibraryScreen -> {
                        NavEntry(key) {
                            LibraryScreen()
                        }
                    }

                    is Destination.PlaylistScreen -> {
                        NavEntry(key) {
                            PlaylistScreen()
                        }
                    }

                    is Destination.ProfileScreen -> {
                        NavEntry(key) {
                            ProfileScreen(
                                isDarkTheme = isDarkTheme
                            ) {
                                onThemeChange()
                            }
                        }
                    }

                    else -> throw RuntimeException("Invalid Navkey...")
                }
            }
        )
    }

}