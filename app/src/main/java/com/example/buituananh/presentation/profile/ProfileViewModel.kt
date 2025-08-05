package com.example.buituananh.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.example.buituananh.data.mapper.toEntity
import com.example.buituananh.data.util.Result
import com.example.buituananh.domain.model.User
import com.example.buituananh.domain.repository.UserRepository
import com.example.buituananh.util.Destination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val key: Destination.ProfileScreen,
    private val repository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _effect = Channel<ProfileEffect>()
    val effect = _effect.receiveAsFlow()

    class Factory(
        private val key: Destination.ProfileScreen,
        private val repository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ProfileViewModel(key, repository) as T
        }
    }

    fun onIntent(intent: ProfileIntent) {
        when(intent) {
            is ProfileIntent.OnDescriptionChange -> onDescriptionChange(intent.description)
            is ProfileIntent.OnNameChange -> onNameChange(intent.name)
            is ProfileIntent.OnPhoneNumberChange -> onPhoneNumberChange(intent.phoneNumber)
            is ProfileIntent.OnUniversityNameChange -> onUniversityChange(intent.university)
            is ProfileIntent.PickImage -> pickImage(intent.uri)
            ProfileIntent.OnSubmitClick -> onSubmitClick()
            ProfileIntent.LoadUserData -> loadUserData()
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            repository.userIdFlow.collect { id ->
                if(id != null) {
                    Log.d("A1", id.toString())
                    _state.update {
                        it.copy(userId = id)
                    }
                    repository.getUserById(userId = id).collect { user ->
                        Log.d("A1", user.toString())
                        _state.update {
                            it.copy(
                                name = user.fullName ?: "",
                                phoneNumber = user.phoneNumber ?: "",
                                universityName = user.universityName ?: "",
                                description = user.description ?: "",
                                avatarUri = user.avatarUri ?: Uri.EMPTY,
                                user = user
                            )
                        }
                    }
                }
            }
        }
    }

    private fun pickImage(uri: Uri?) {
        if(uri == null) {
            sendEvent(ProfileEffect.ShowToast("Pick image unsuccessfully"))
        } else {
            _state.update {
                it.copy(avatarUri = uri)
            }
        }
    }

    private fun onSubmitClick() {
        viewModelScope.launch {
            val name = _state.value.name
            val phoneNumber = _state.value.phoneNumber
            val universityName = _state.value.universityName
            val regex = Regex("^[a-zA-Z]+( [a-zA-Z]+)*$")
            val phoneRegex = Regex("^\\d*$")

            val isNameError = name.isEmpty() || !name.matches(regex)
            val isUniversityError = universityName.isEmpty() || !universityName.matches(regex)
            val isPhoneNumberError = phoneNumber.isEmpty() || !phoneNumber.matches(phoneRegex)

            val hasError = isNameError || isUniversityError || isPhoneNumberError

            _state.update {
                it.copy(
                    isNameError = isNameError,
                    isUniversityError = isUniversityError,
                    isPhoneNumberError = isPhoneNumberError
                )
            }

            if (!hasError) {
                val user = User(
                    userId = _state.value.userId,
                    username = _state.value.user?.username ?: "",
                    password = _state.value.user?.password ?: "",
                    email = _state.value.user?.email ?: "",
                    fullName = name,
                    phoneNumber = phoneNumber,
                    description = _state.value.description,
                    avatarUri = _state.value.avatarUri,
                    universityName = _state.value.universityName
                )
                val result = repository.updateUser(user.toEntity())
                if(result is Result.Success) {
                    sendEvent(ProfileEffect.ShowDialog)
                } else if(result is Result.Failure) {
                    sendEvent(ProfileEffect.ShowToast(result.error.message ?: "Unknown error"))
                }
            }
        }
    }

    private fun onUniversityChange(university: String) {
        _state.update {
            it.copy(universityName = university)
        }
    }

    private fun onPhoneNumberChange(phoneNumber: String) {
        _state.update {
            it.copy(phoneNumber = phoneNumber)

        }
    }

    private fun onNameChange(name: String) {
        _state.update {
            it.copy(name = name)
        }
    }

    private fun onDescriptionChange(description: String) {
        _state.update {
            it.copy(description = description)
        }
    }

    private fun sendEvent(effect: ProfileEffect) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

}