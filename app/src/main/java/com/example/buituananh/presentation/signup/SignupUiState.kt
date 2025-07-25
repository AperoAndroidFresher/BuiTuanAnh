package com.example.buituananh.presentation.signup

data class SignupUiState(
    val username: String = "",
    val password: String = "",
    val confirmedPassword: String = "",
    val email: String = "",

    val usernameError: String = "",
    val passwordError: String = "",
    val confirmedPasswordError: String = "",
    val emailError: String = "",

)
