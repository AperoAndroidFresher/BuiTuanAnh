package com.example.buituananh.presentation.signup

data class SignupState(
    val username: String = "",
    val password: String = "",
    val confirmedPassword: String = "",
    val email: String = "",

    val usernameError: String = "",
    val passwordError: String = "",
    val confirmedPasswordError: String = "",
    val emailError: String = "",
)


sealed interface SignupIntent {
    data class OnUsernameChange(val username: String) : SignupIntent
    data class OnPasswordChange(val password: String) : SignupIntent
    data class OnConfirmedPasswordChange(val confirmedPassword: String) : SignupIntent
    data class OnEmailChange(val email: String) : SignupIntent
    data object OnSubmitClick : SignupIntent
    data object OnLoginClick : SignupIntent
}

sealed interface SignupEffect {
    data object NavigateToLoginScreen : SignupEffect
    data object PopBack : SignupEffect
    data class ShowToast(val message: String) : SignupEffect
}

