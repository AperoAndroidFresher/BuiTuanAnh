package com.example.buituananh.presentation.signup

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.buituananh.model.User
import com.example.buituananh.model.UserStore
import kotlinx.coroutines.channels.Channel

class SignupUiController {

    private val _state = mutableStateOf<SignupUiState>(SignupUiState())
    val state: MutableState<SignupUiState>
        get() = _state

    fun onEvent(event: SignupEvent): Boolean {
        return when(event) {
            is SignupEvent.OnConfirmedPasswordChange -> {
                onConfirmedPasswordChange(event.confirmedPassword.filterNot { it.isWhitespace() })
                false
            }
            is SignupEvent.OnEmailChange -> {
                onEmailChange(event.email.filterNot { it.isWhitespace() })
                false
            }
            is SignupEvent.OnPasswordChange -> {
                onPasswordChange(event.password.filterNot { it.isWhitespace() })
                false
            }
            is SignupEvent.OnUsernameChange -> {
                onUsernameChange(event.username.lowercase().filterNot { it.isWhitespace() })
                false
            }
            SignupEvent.Submit -> submit()
        }
    }

    private fun onEmailChange(email: String) {
        if(_state.value.emailError.isNotBlank()) {
            _state.value = _state.value.copy(
                emailError = ""
            )
        }
        _state.value = _state.value.copy(email = email)
    }

    private fun onConfirmedPasswordChange(confirmedPassword: String) {
        if(_state.value.confirmedPasswordError.isNotBlank()) {
            _state.value = _state.value.copy(
                confirmedPasswordError = ""
            )
        }
        _state.value = _state.value.copy(confirmedPassword = confirmedPassword)
    }


    private fun onUsernameChange(username: String) {
        if(_state.value.usernameError.isNotBlank()) {
            _state.value = _state.value.copy(
                usernameError = ""
            )
        }
        _state.value = _state.value.copy(username = username)
    }

    private fun onPasswordChange(password: String) {
        if(_state.value.passwordError.isNotBlank()) {
            _state.value = _state.value.copy(
                passwordError = ""
            )
        }
        _state.value = _state.value.copy(password = password)
    }

    private fun submit(): Boolean {
        val usernameRegex = Regex("^[a-z\\d]*$")
        val passwordRegex = Regex("^[a-zA-Z\\d]*$")
        val emailRegex = Regex("^[a-z0-9._-]+@apero\\.vn\$")

        val username = _state.value.username
        val password = _state.value.password
        val confirmedPassword = _state.value.confirmedPassword
        val email = _state.value.email

        var hasError = false

        if(!username.matches(usernameRegex) || username.isBlank()) {
            hasError = true
            _state.value = _state.value.copy(
                usernameError = "Invalid format",
                username = ""
            )
        } else {
            _state.value = _state.value.copy(
                usernameError = ""
            )
        }
        if(password.isBlank() || !password.matches(passwordRegex) ) {
            hasError = true
            _state.value = _state.value.copy(
                passwordError = "Invalid format",
                password = "",
            )
        } else {
            _state.value = _state.value.copy(
                passwordError = ""
            )
        }
        if((password != confirmedPassword) || confirmedPassword.isBlank()) {
            hasError = true
            _state.value = _state.value.copy(
                confirmedPasswordError = "Confirmation password does not match",
                confirmedPassword =  "",
            )
        } else {
            _state.value = _state.value.copy(
                confirmedPasswordError = ""
            )
        }
        if(email.isBlank() || !email.matches(emailRegex)) {
            hasError = true
            _state.value = _state.value.copy(
                emailError = "Invalid format",
                email = "",
            )
        } else {
            _state.value = _state.value.copy(
                emailError = ""
            )
        }
        if(!hasError) {
            val user = User(username = username, password = password, email = email)
            UserStore.userList.add(user)
        }
        return hasError
    }

}