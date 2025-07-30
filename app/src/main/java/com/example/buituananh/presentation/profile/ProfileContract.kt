package com.example.buituananh.presentation.profile

import android.net.Uri

data class ProfileState(
    val name: String = "",
    val phoneNumber: String = "",
    val universityName: String = "",
    val description: String = "",
    val isNameError: Boolean = false,
    val isPhoneNumberError: Boolean = false,
    val isUniversityError: Boolean = false,
    val uriPicker: Uri = Uri.EMPTY
)

sealed interface ProfileIntent {
    data class OnNameChange(val name: String) : ProfileIntent
    data class OnPhoneNumberChange(val phoneNumber: String) : ProfileIntent
    data class OnUniversityNameChange(val university: String) : ProfileIntent
    data class OnDescriptionChange(val description: String) : ProfileIntent
    data object OnSubmitClick : ProfileIntent
    data class PickImage(val uri: Uri?) : ProfileIntent
}

sealed interface ProfileEffect {
    data object ShowDialog : ProfileEffect
    data class ShowToast(val message: String) : ProfileEffect
}