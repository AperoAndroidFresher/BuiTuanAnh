package com.example.buituananh.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.model.UserStore
import com.example.buituananh.util.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    val key: Destination.LoginScreen
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    class Factory(
        private val key: Destination.LoginScreen
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LoginViewModel(key) as T
        }
    }

    fun onIntent(intent: LoginIntent) {
        return when (intent) {
            is LoginIntent.OnPasswordChange -> onPasswordChange(intent.password)

            is LoginIntent.OnUsernameChange -> onUsernameChange(intent.username)

            is LoginIntent.OnCheckedChange -> onCheckedChange(intent.checked)

            LoginIntent.OnLoginClick -> login()

            LoginIntent.OnSignupClick -> navToSignup()
        }
    }

    private fun navToSignup() {
        sendEvent(LoginEffect.NavigateToSignupScreen)
    }

    private fun onCheckedChange(checked: Boolean) {
        _state.value = _state.value.copy(isChecked = checked)
    }

    private fun onUsernameChange(username: String) {
        val filtered = username.lowercase().filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                username = filtered,
                usernameError = ""
            )
        }
    }

    private fun onPasswordChange(password: String) {
        val filtered = password.filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                password = filtered,
                passwordError = ""
            )
        }
    }

    private fun login() {
        val usernameRegex = Regex("^[a-z\\d]*$")
        val passwordRegex = Regex("^[a-zA-Z\\d]*$")
        val username = _state.value.username
        val password = _state.value.password

        var hasError = false

        if (!username.matches(usernameRegex) || username.isBlank()) {
            hasError = true
            _state.update {
                it.copy(
                    usernameError = "Invalid username format",
                    username = ""
                )
            }
        } else {
            _state.update {
                it.copy(
                    usernameError = "",
                    username = ""
                )
            }
        }

        if (!password.matches(passwordRegex) || password.isBlank()) {
            hasError = true
            _state.update {
                it.copy(
                    passwordError = "Invalid password format",
                    password = ""
                )
            }
        } else {
            _state.update {
                it.copy(
                    passwordError = "",
                )
            }
        }

        if (!hasError) {
            val matchedUser = UserStore.userList.find {
                it.username == username && it.password == password
            }

            if (matchedUser == null) {
                hasError = true
                _state.update {
                    it.copy(
                        username = "",
                        password = "",
                        usernameError = "Username or password is incorrect",
                        passwordError = "Username or password is incorrect"
                    )
                }
            }
        }

        if (!hasError) {
            sendEvent(LoginEffect.NavigateToHomeScreen)
        } else {
            sendEvent(LoginEffect.ShowToast("Login unsuccessfully"))
        }
    }

    private fun sendEvent(event: LoginEffect) {
        viewModelScope.launch {
            _effect.send(event)
        }
    }

}