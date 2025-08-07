package com.example.buituananh.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.buituananh.di.AppContainer
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.presentation.login.component.LoginScreenRoot
import com.example.buituananh.presentation.signup.SignupViewModel
import com.example.buituananh.presentation.signup.component.SignupScreenRoot
import com.example.buituananh.presentation.splash.SplashScreen
import com.example.buituananh.util.Destination

@Composable
fun AuthWrapperEntry(
    addToBackStack: (Destination) -> Unit,
    onBack: () -> Unit,
    loginViewModel: LoginViewModel,
    appContainer: AppContainer,
    backStack: NavBackStack
) {
    
    val authBackstack = rememberNavBackStack(Destination.SplashScreen)
    
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
                            addToBackStack(Destination.HomeScreen)
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
                            onBack()
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
