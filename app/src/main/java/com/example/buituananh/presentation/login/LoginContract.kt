package com.example.buituananh.presentation.login

data class LoginState(
    val username: String = "",
    val password: String = "",
    val isChecked: Boolean = false,

    val usernameError: String = "",
    val passwordError: String = ""
)

sealed interface LoginIntent {
    data class OnUsernameChange(val username: String) : LoginIntent
    data class OnPasswordChange(val password: String) : LoginIntent
    data class OnCheckedChange(val checked: Boolean) : LoginIntent
    data object OnLoginClick : LoginIntent
    data object OnSignupClick : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateToSignupScreen : LoginEffect
    data object NavigateToHomeScreen : LoginEffect
    data class ShowToast(val message: String) : LoginEffect
}
