package com.example.buituananh.presentation.signup

sealed class SignupEvent {

    data class OnUsernameChange(val username: String) : SignupEvent()
    data class OnPasswordChange(val password: String) : SignupEvent()
    data class OnConfirmedPasswordChange(val confirmedPassword: String) : SignupEvent()
    data class OnEmailChange(val email: String) : SignupEvent()
    data object Submit : SignupEvent()

}