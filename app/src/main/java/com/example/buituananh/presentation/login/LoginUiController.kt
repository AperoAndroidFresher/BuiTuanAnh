package com.example.buituananh.presentation.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.buituananh.model.UserStore

class LoginUiController {

    private val _state = mutableStateOf<LoginUiState>(LoginUiState())
    val state: MutableState<LoginUiState>
        get() = _state

    fun onEvent(event: LoginEvent): Boolean {
        return when (event) {
            is LoginEvent.OnPasswordChange -> {
                onPasswordChange(event.password.filterNot { it.isWhitespace() })
                false
            }

            is LoginEvent.OnUsernameChange -> {
                onUsernameChange(event.username.lowercase().filterNot { it.isWhitespace() })
                false
            }

            is LoginEvent.OnCheckedChange -> {
                onCheckedChange(event.checked)
                false
            }

            LoginEvent.Login -> login()
        }
    }

    private fun onCheckedChange(checked: Boolean) {
        _state.value = _state.value.copy(isChecked = checked)
    }

    private fun onUsernameChange(username: String) {
        if (_state.value.usernameError.isNotBlank()) {
            _state.value = _state.value.copy(
                usernameError = ""
            )
        }
        _state.value = _state.value.copy(username = username)
    }

    private fun onPasswordChange(password: String) {
        if (_state.value.passwordError.isNotBlank()) {
            _state.value = _state.value.copy(
                passwordError = ""
            )
        }
        _state.value = _state.value.copy(password = password)
    }

    private fun login(): Boolean {
        val usernameRegex = Regex("^[a-z\\d]*$")
        val passwordRegex = Regex("^[a-zA-Z\\d]*$")
        val username = _state.value.username
        val password = _state.value.password

        var hasError = false

        if (!username.matches(usernameRegex) || username.isBlank()) {
            hasError = true
            _state.value = _state.value.copy(
                usernameError = "Invalid username format",
                username = ""
            )
        } else {
            _state.value = _state.value.copy(usernameError = "")
        }

        if (!password.matches(passwordRegex) || password.isBlank()) {
            hasError = true
            _state.value = _state.value.copy(
                passwordError = "Invalid password format",
                password = ""
            )
        } else {
            _state.value = _state.value.copy(passwordError = "")
        }

        if (!hasError) {
            val matchedUser = UserStore.userList.find {
                it.username == username && it.password == password
            }

            if (matchedUser == null) {
                hasError = true
                _state.value = _state.value.copy(
                    usernameError = "Username or password is incorrect",
                    passwordError = "Username or password is incorrect"
                )
            }
        }

        return hasError
    }

}