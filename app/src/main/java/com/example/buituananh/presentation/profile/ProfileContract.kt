package com.example.buituananh.presentation.profile

import android.net.Uri
import com.example.buituananh.domain.model.User

data class ProfileState(
    val userId: Long = 0,
    val name: String = "",
    val phoneNumber: String = "",
    val avatarUri: Uri = Uri.EMPTY,
    val universityName: String = "",
    val description: String = "",
    val user: User? = null,

    val isNameError: Boolean = false,
    val isPhoneNumberError: Boolean = false,
    val isUniversityError: Boolean = false,
)

sealed interface ProfileIntent {
    data class OnNameChange(val name: String) : ProfileIntent
    data class OnPhoneNumberChange(val phoneNumber: String) : ProfileIntent
    data class OnUniversityNameChange(val university: String) : ProfileIntent
    data class OnDescriptionChange(val description: String) : ProfileIntent
    data object OnSubmitClick : ProfileIntent
    data class PickImage(val uri: Uri?) : ProfileIntent
    data object LoadUserData : ProfileIntent
    data object LogOut : ProfileIntent
}

sealed interface ProfileEffect {
    data object ShowDialog : ProfileEffect
    data object PopBack : ProfileEffect
    data class ShowToast(val message: String) : ProfileEffect
}
