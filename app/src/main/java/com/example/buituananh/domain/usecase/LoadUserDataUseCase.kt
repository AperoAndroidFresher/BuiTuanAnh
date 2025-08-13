package com.example.buituananh.domain.usecase

import com.example.buituananh.domain.model.User
import com.example.buituananh.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class LoadUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<User> {
        return userRepository.userIdFlow.filterNotNull().flatMapLatest { userId ->
            userRepository.getUserById(userId)
        }
    }
    
}
