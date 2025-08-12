package com.example.buituananh.presentation.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.buituananh.domain.usecase.ApplyLanguageUseCase
import com.example.buituananh.domain.usecase.LoadLanguageUseCase
import com.example.buituananh.presentation.home.HomeViewModel
import com.example.buituananh.util.Destination
import com.example.buituananh.util.Language
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SettingViewModel.Factory::class)
class SettingViewModel @AssistedInject constructor(
    @Assisted private val navKey: Destination.SettingScreen,
    private val loadLanguageUseCase: LoadLanguageUseCase,
    private val applyLanguageUseCase: ApplyLanguageUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SettingEffect>()
    val effect = _effect.receiveAsFlow()

    fun onIntent(intent: SettingIntent) {
        when (intent) {
            SettingIntent.LoadLanguage -> loadLanguage()
            SettingIntent.CancelLanguage -> cancelLanguage()
            is SettingIntent.AcceptLanguage -> acceptLanguage()
            is SettingIntent.OnLanguageChange -> onLanguageChange(intent.language)
        }
    }

    private fun onLanguageChange(language: Language) {
        _state.update {
            it.copy(
                backupLanguageCode = it.backupLanguageCode ?: it.currentLanguageCode,
                currentLanguageCode = language.languageCode
            )
        }
    }

    private fun acceptLanguage() {
        viewModelScope.launch {
            sendEffect(SettingEffect.PopBack)
            _state.update { 
                it.copy(
                    backupLanguageCode = null
                )
            }
            applyLanguageUseCase(_state.value.currentLanguageCode)
        }
    }

    private fun cancelLanguage() {
        _state.update {
            it.copy(
                currentLanguageCode = it.backupLanguageCode ?: "en",
                backupLanguageCode = null
            )
        }
        sendEffect(SettingEffect.PopBack)
    }

    private fun loadLanguage() {
        viewModelScope.launch {
            loadLanguageUseCase().collect { language ->
                Log.d("SettingViewModel", "loadLanguage: $language")
                language?.let { newLanguage ->
                    _state.update {
                        it.copy(
                            currentLanguageCode = newLanguage,
                        )
                    }
                }
            }
        }
    }

    private fun sendEffect(effect: SettingEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(navKey: Destination.SettingScreen): SettingViewModel
    }
}
