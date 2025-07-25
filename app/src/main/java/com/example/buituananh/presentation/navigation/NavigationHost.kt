package com.example.buituananh.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.buituananh.presentation.login.screen.LoginScreen
import com.example.buituananh.presentation.playlist.PlaylistScreen
import com.example.buituananh.presentation.signup.screen.SignupScreen
import com.example.buituananh.presentation.splash.SplashScreen
import com.example.buituananh.util.Route

@Composable
fun NavigationHost(modifier: Modifier = Modifier) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SPLASH_SCREEN.toString(),
        modifier = modifier
    ) {
        composable(Route.SPLASH_SCREEN.toString()) {
            SplashScreen {
                navController.navigate(Route.LOGIN_SCREEN.toString()) {
                    popUpTo(Route.LOGIN_SCREEN.toString())
                }
            }
        }

        composable(Route.LOGIN_SCREEN.toString()) {
            LoginScreen {
                navController.navigate(it) {
                    launchSingleTop = true
                }
            }
        }

        composable(Route.SIGNUP_SCREEN.toString()) {
            SignupScreen(
                onPopBack = {
                    navController.popBackStack()
                }
            ) {
                navController.navigate(it) {
                    launchSingleTop = true
                }
            }
        }

        composable(Route.PLAYLIST_SCREEN.toString()) {
            PlaylistScreen()
        }

    }

}