package com.example.buituananh.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.util.onError
import com.example.buituananh.data.util.onSuccess
import com.example.buituananh.domain.usecase.FetchAlbumTrackArtistUseCase
import com.example.buituananh.domain.usecase.LoadUserDataUseCase
import com.example.buituananh.util.Destination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted private val navKey: Destination.HomeWrapper,
    private val loadUserDataUseCase: LoadUserDataUseCase,
    private val fetchAlbumTrackArtistUseCase: FetchAlbumTrackArtistUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    
    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()
    
    init {
        loadAlbumTrackArtist()
        loadUserData()
    }
    
    fun onIntent(intent: HomeIntent) {
        when(intent) {
            HomeIntent.ClickProfile -> clickProfile() 
            HomeIntent.ClickSetting -> clickSetting()
            HomeIntent.LoadUserData -> loadUserData()
            HomeIntent.LoadAlbumTrackArtist -> loadAlbumTrackArtist()
            HomeIntent.ClickSeeAllAlbums -> clickSeeAllAlbums()
            HomeIntent.ClickSeeAllArtists -> clickSeeAllArtist()
            HomeIntent.ClickSeeAllTracks -> clickSeeAllTracks()
        }
    }

    private fun clickSeeAllTracks() {
        viewModelScope.launch { 
            sendEffect(HomeEffect.NavigateToTracksScreen(_state.value.tracks))
        }
    }

    private fun clickSeeAllArtist() {
        viewModelScope.launch {
            sendEffect(HomeEffect.NavigateToArtistScreen(_state.value.artists))
        }
    }

    private fun clickSeeAllAlbums() {
        viewModelScope.launch {
            sendEffect(HomeEffect.NavigateToAlbumsScreen(_state.value.albums))
        }
    }

    private fun loadAlbumTrackArtist() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            delay(1000L)
            fetchAlbumTrackArtistUseCase().apply { 
                onSuccess { (albums, tracks, artist) -> 
                    _state.update { 
                        it.copy(
                            albums = albums,
                            tracks = tracks,
                            artists = artist,
                            isLoading = false
                        )
                    }
                }
                onError { e -> 
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = e.localizedMessage
                        )
                    }
                    Log.d("HomeViewModel", "loadAlbumTrackArtist: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            loadUserDataUseCase().collect { user ->
                _state.update { it.copy(user = user) }
                Log.d("userflow1", "loadUserData: ${user.toString()}")
            }
        }
    }

    private fun clickSetting() {
        sendEffect(HomeEffect.NavigateToSettingScreen)
    }

    private fun clickProfile() {
        sendEffect(HomeEffect.NavigateToProfileScreen)        
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch { 
            _effect.send(effect)
        }
    }
    
    @AssistedFactory
    interface Factory {
        fun create(navKey: Destination.HomeWrapper): HomeViewModel
    }
        
} 
