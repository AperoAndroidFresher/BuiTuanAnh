package com.example.buituananh.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.buituananh.presentation.home.components.AvatarTopBar
import com.example.buituananh.presentation.home.components.HomeEffect
import com.example.buituananh.presentation.home.components.HomeIntent
import com.example.buituananh.presentation.home.components.HomeState
import com.example.buituananh.ui.theme.BuiTuanAnhTheme
import com.example.buituananh.util.Destination

@Composable
fun HomeScreenRoot(
    onNavigate: (Destination) -> Unit,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {

    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when(effect) {
                HomeEffect.NavigateToProfileScreen -> onNavigate(Destination.ProfileScreen)
                HomeEffect.NavigateToSettingScreen -> onNavigate(Destination.SettingScreen)
                is HomeEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    LaunchedEffect(Unit) {
        viewModel.onIntent(HomeIntent.LoadUserData)
    }
    
    HomeScreen(
        state = state,
        onIntent = viewModel::onIntent,
        modifier = modifier
    )

}

@Composable
fun HomeScreen(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            AvatarTopBar(
                clickSetting = {
                    onIntent(HomeIntent.ClickSetting)
                },
                clickAvatar = {
                    onIntent(HomeIntent.ClickProfile)
                },
                avatarUri = state.user?.avatarUri,
                userName = state.user?.username
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it),
        ) {
            
        }
    }
    
}

@Preview
@Composable
private fun PreviewHomeScreen() {
    BuiTuanAnhTheme { 
        HomeScreen(
            state = HomeState(),
            onIntent = {}
        )
    }
}
