package com.example.buituananh.presentation.login

data class LoginState(
    val userName: String = "",
    val passWord: String = "",
    val isRemembered: Boolean = false,

    val userNameError: String = "",
    val passWordError: String = ""
)

sealed interface LoginIntent {
    data class OnUsernameChange(val username: String) : LoginIntent
    data class OnPasswordChange(val password: String) : LoginIntent
    data class OnCheckedChange(val checked: Boolean) : LoginIntent
    data object IsRememberedLogin : LoginIntent
    data object ClickLogin : LoginIntent
    data object ClickSignup : LoginIntent
}

sealed interface LoginEffect {
    data object NavigateToSignupScreen : LoginEffect
    data object NavigateToHomeScreen : LoginEffect
    data class ShowToast(val message: String) : LoginEffect
}

sealed interface SplashEffect {
    data object NavigateToLoginScreen : SplashEffect
    data object NavigateToHomeScreen : SplashEffect
}
