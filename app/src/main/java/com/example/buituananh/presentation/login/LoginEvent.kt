package com.example.buituananh.presentation.login

sealed class LoginEvent {

    data class OnUsernameChange(val username: String) : LoginEvent()
    data class OnPasswordChange(val password: String) : LoginEvent()
    data class OnCheckedChange(val checked: Boolean) : LoginEvent()
    data object Login : LoginEvent()

}