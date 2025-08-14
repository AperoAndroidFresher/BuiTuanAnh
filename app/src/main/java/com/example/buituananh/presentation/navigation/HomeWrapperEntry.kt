package com.example.buituananh.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.buituananh.presentation.home.HomeViewModel
import com.example.buituananh.presentation.home.components.HomeScreenRoot
import com.example.buituananh.presentation.home.components.TopAlbumsScreen
import com.example.buituananh.presentation.home.components.TopArtistScreen
import com.example.buituananh.presentation.home.components.TopTrackScreen
import com.example.buituananh.presentation.profile.ProfileViewModel
import com.example.buituananh.presentation.profile.component.ProfileScreenRoot
import com.example.buituananh.presentation.setting.SettingScreen
import com.example.buituananh.presentation.setting.SettingViewModel
import com.example.buituananh.util.Destination

@Composable
fun HomeWrapperEntry(
    homeViewModel: HomeViewModel,
    homeBackStack: NavBackStack,
    addDestination: (Destination) -> Unit,
    onBack: () -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {

    
    NavDisplay(
        backStack = homeBackStack,
        onBack = {
            homeBackStack.removeLastOrNull()
        },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider { 
            entry<Destination.HomeScreen> {
                HomeScreenRoot(
                    onNavigate = {
                        addDestination(it)
                    },
                    viewModel = homeViewModel
                )
            } 
            entry<Destination.AlbumsScreen> { key ->
                TopAlbumsScreen(
                    popBack = { onBack() },
                    albums = key.albums
                )
            }
            entry<Destination.TrackScreen> { key ->
                TopTrackScreen(
                    popBack = { onBack() },
                    tracks = key.tracks
                )
            }
            entry<Destination.ArtistScreen> { key ->
                TopArtistScreen(
                    popBack = { onBack() },
                    artists = key.artists
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
                        onBack()
                    }
                )
            }
            entry<Destination.ProfileScreen> { key ->
                val viewModel = hiltViewModel<ProfileViewModel, ProfileViewModel.Factory>(
                    creationCallback = { factory ->
                        factory.create(key)
                    },
                )
                ProfileScreenRoot(
                    popBack = navigateToLogin,
                    viewModel = viewModel,
                )
            }
        }
    ) 
        
}
