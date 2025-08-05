package com.example.buituananh.presentation.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.buituananh.data.local.mapper.toEntity
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.User
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.util.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignupViewModel(
    private val key: Destination.SignupScreen,
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignupState())
    val state = _state.asStateFlow()

    private val _effect = Channel<SignupEffect>()
    val effect = _effect.receiveAsFlow()

    class Factory(
        private val key: Destination.SignupScreen,
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignupViewModel(key, repository) as T
        }
    }

    fun onIntent(intent: SignupIntent) {
        return when (intent) {
            is SignupIntent.OnConfirmedPasswordChange -> onConfirmedPasswordChange(intent.confirmedPassword)

            is SignupIntent.OnEmailChange -> onEmailChange(intent.email)

            is SignupIntent.OnPasswordChange -> onPasswordChange(intent.password)

            is SignupIntent.OnUsernameChange -> onUsernameChange(intent.username)

            SignupIntent.OnLoginClick -> onLoginClick()

            SignupIntent.OnSubmitClick -> onSubmitClick()
        }
    }

    private fun onLoginClick() {
        sendEvent(SignupEffect.PopBack)
    }

    private fun onEmailChange(email: String) {
        val filtered = email.lowercase().filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                email = filtered,
                emailError = ""
            )
        }
    }

    private fun onConfirmedPasswordChange(confirmedPassword: String) {
        val filtered = confirmedPassword.lowercase().filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                confirmedPassword = filtered,
                confirmedPasswordError = ""
            )
        }
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
        val filtered = password.lowercase().filterNot { it.isWhitespace() }
        _state.update {
            it.copy(
                password = filtered,
                passwordError = ""
            )
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            val usernameRegex = Regex("^[a-z\\d]*$")
            val passwordRegex = Regex("^[a-zA-Z\\d]*$")
            val emailRegex = Regex("^[a-z0-9._-]+@apero\\.vn\$")

            val username = _state.value.username
            val password = _state.value.password
            val confirmedPassword = _state.value.confirmedPassword
            val email = _state.value.email

            var hasError = false

            if (!username.matches(usernameRegex) || username.isBlank()) {
                hasError = true
                _state.update {
                    it.copy(
                        usernameError = "Invalid format",
                        username = ""
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        usernameError = "",
                    )
                }
            }
            if (password.isBlank() || !password.matches(passwordRegex)) {
                hasError = true
                _state.update {
                    it.copy(
                        passwordError = "Invalid format",
                        password = "",
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        passwordError = "",
                    )
                }
            }
            if ((password != confirmedPassword) || confirmedPassword.isBlank()) {
                hasError = true
                _state.update {
                    it.copy(
                        confirmedPasswordError = "Confirmation password does not match",
                        confirmedPassword = "",
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        confirmedPasswordError = "",
                    )
                }
            }
            if (email.isBlank() || !email.matches(emailRegex)) {
                hasError = true
                _state.update {
                    it.copy(
                        emailError = "Invalid format",
                        email = "",
                    )
                }
            } else {
                _state.update {
                    it.copy(
                        emailError = "",
                    )
                }
            }
            if (!hasError) {
                val user = User(
                    username = username,
                    password = password,
                    email = email
                )
                val result = repository.userRegistration(user = user.toEntity())
                if(result is Result.Failure) {
                    sendEvent(SignupEffect.ShowToast(result.error.message ?: "Unknown error"))
                    Log.d("ERROR1", result.error.message.toString())
                } else if(result is Result.Success) {
                    sendEvent(SignupEffect.ShowToast(result.data))
                    sendEvent(SignupEffect.NavigateToLoginScreen)
                }
            }
        }
    }

    private fun sendEvent(event: SignupEffect) {
        viewModelScope.launch {
            _effect.send(event)
        }
    }

}
