package com.example.buituananh.presentation.login

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isChecked: Boolean = false,

    val usernameError: String = "",
    val passwordError: String = ""

)
