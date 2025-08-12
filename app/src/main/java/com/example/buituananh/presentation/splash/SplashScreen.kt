package com.example.buituananh.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.buituananh.R
import com.example.buituananh.presentation.login.LoginIntent
import com.example.buituananh.presentation.login.LoginViewModel
import com.example.buituananh.presentation.login.SplashEffect
import com.example.buituananh.util.Destination
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: LoginViewModel,
    onNavigate: (Destination) -> Unit,
    modifier: Modifier = Modifier
) {
    
    LaunchedEffect(Unit) {
        viewModel.onIntent(LoginIntent.IsRememberedLogin)
        viewModel.splashEffect.collect { effect ->
            when(effect) {
                SplashEffect.NavigateToHomeScreen -> {
                    delay(1000L)
                    onNavigate(Destination.HomeWrapper)
                }
                SplashEffect.NavigateToLoginScreen -> {
                    delay(1000L)
                    onNavigate(Destination.LoginScreen)
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.aperologo),
            contentDescription = null,
            modifier = Modifier.size(300.dp)
        )
    }

}
