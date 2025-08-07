package com.example.buituananh.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.util.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val key: Destination.AuthWrapper,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _loginEffect = Channel<LoginEffect>()
    val loginEffect = _loginEffect.receiveAsFlow()

    private val _splashEffect = Channel<SplashEffect>()
    val splashEffect = _splashEffect.receiveAsFlow()
    
    fun onIntent(intent: LoginIntent) {
        return when (intent) {
            is LoginIntent.OnPasswordChange -> onPasswordChange(intent.password)

            is LoginIntent.OnUsernameChange -> onUsernameChange(intent.username)

            is LoginIntent.OnCheckedChange -> onCheckedChange(intent.checked)

            LoginIntent.ClickLogin -> clickLogin()

            LoginIntent.ClickSignup -> clickSignup()
            
            LoginIntent.IsRememberedLogin -> isRememberedLogin()

        }
    }

    private fun setRememberedLogin() {
        viewModelScope.launch {
            if(_state.value.isRemembered) {
                userRepository.setRememberedLoginState(true)
            }
        }
    }

    private fun isRememberedLogin() {
        viewModelScope.launch {
            val isRemembered = userRepository.isRememberedLoginEnabled()
            if(isRemembered) {
                _splashEffect.send(SplashEffect.NavigateToHomeScreen)
            } else {
                _splashEffect.send(SplashEffect.NavigateToLoginScreen)
            }   
        }
        
    }

    private fun clickSignup() {
        sendEvent(LoginEffect.NavigateToSignupScreen)
    }

    private fun onCheckedChange(checked: Boolean) {
        _state.value = _state.value.copy(isRemembered = checked)
    }

    private fun onUsernameChange(username: String) {
        val filtered = username.lowercase().filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                userName = filtered,
                userNameError = ""
            )
        }
    }

    private fun onPasswordChange(password: String) {
        val filtered = password.filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                passWord = filtered,
                passWordError = ""
            )
        }
    }

    private fun clickLogin() {
        viewModelScope.launch {
            val username = _state.value.userName
            val password = _state.value.passWord

            val isUsernameValid = validateUsername(username)
            val isPasswordValid = validatePassword(password)

            if (!isUsernameValid || !isPasswordValid) {
                sendEvent(LoginEffect.ShowToast("Login unsuccessfully"))
                return@launch
            }

            val result = userRepository.assertLogin(username, password)
            if (result is Result.Failure) {
                handleLoginFailure(result.error)
                sendEvent(LoginEffect.ShowToast("Login unsuccessfully"))
                return@launch
            }

            if (result is Result.Success) {
                userRepository.saveUserId(result.data.userId)
                setRememberedLogin()
                sendEvent(LoginEffect.NavigateToHomeScreen)
            }
        }
    }

    private fun validateUsername(username: String): Boolean {
        val regex = Regex("^[a-z\\d]*$")
        val isValid = username.matches(regex) && username.isNotBlank()
        _state.update {
            it.copy(
                userNameError = if (!isValid) "Invalid username format" else "",
                userName = if (!isValid) "" else it.userName
            )
        }
        return isValid
    }

    private fun validatePassword(password: String): Boolean {
        val regex = Regex("^[a-zA-Z\\d]*$")
        val isValid = password.matches(regex) && password.isNotBlank()
        _state.update {
            it.copy(
                passWordError = if (!isValid) "Invalid password format" else "",
                passWord = if (!isValid) "" else it.passWord
            )
        }
        return isValid
    }

    private fun handleLoginFailure(error: Throwable) {
        val errorMessage = error.localizedMessage ?: "Unknown error"
        _state.update {
            it.copy(
                userName = "",
                passWord = "",
                userNameError = errorMessage,
                passWordError = errorMessage
            )
        }
    }

    private fun sendEvent(event: LoginEffect) {
        viewModelScope.launch {
            _loginEffect.send(event)
        }
    }
    
    private fun sendSplashEvent( event: SplashEffect) {
        viewModelScope.launch { 
            _splashEffect.send(event)
        }
    }

    class Factory(
        private val key: Destination.AuthWrapper,
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(key, repository) as T
        }
    }

}
