package com.example.buituananh.presentation.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
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
                        if(it is Destination.HomeWrapper) {
                            addToBackStack(Destination.HomeWrapper)
                        }
                    },
                    viewModel = loginViewModel
                )
            }
            entry<Destination.LoginScreen> { key: Destination.LoginScreen ->
                LoginScreenRoot(
                    viewModel = loginViewModel
                ) { route ->
                    if (route is Destination.HomeWrapper) {
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
                val viewModel = hiltViewModel<SignupViewModel, SignupViewModel.Factory>(
                    creationCallback = { factory -> 
                        factory.create(key)
                    }
                )
                SignupScreenRoot(
                    viewModel = viewModel,
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
