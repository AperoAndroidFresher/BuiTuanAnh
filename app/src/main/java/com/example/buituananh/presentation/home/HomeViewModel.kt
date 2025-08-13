package com.example.buituananh.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buituananh.domain.usecase.LoadUserDataUseCase
import com.example.buituananh.presentation.home.components.HomeEffect
import com.example.buituananh.presentation.home.components.HomeIntent
import com.example.buituananh.presentation.home.components.HomeState
import com.example.buituananh.util.Destination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted private val navKey: Destination.HomeWrapper,
    private val loadUserDataUseCase: LoadUserDataUseCase
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
    
    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()
    
    fun onIntent(intent: HomeIntent) {
        when(intent) {
            HomeIntent.ClickProfile -> clickProfile() 
            HomeIntent.ClickSetting -> clickSetting()
            HomeIntent.LoadUserData -> loadUserData()
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            loadUserDataUseCase().collect { user ->
                _state.update { it.copy(user = user) }
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
