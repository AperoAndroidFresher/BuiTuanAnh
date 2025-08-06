package com.example.buituananh.data.util

import kotlin.reflect.KFunction

sealed interface Result<out T, out E: Exception> {
    data class Success<out T>(val data: T) : Result<T, Nothing> 
    data class Failure<out E: Exception>(val error: E) : Result<Nothing, E>
}

inline fun <T, E: Exception> Result<T, E>.onSuccess(action: (T) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Failure -> this
        is Result.Success -> {
            action(data)
            this
        }
    }
}

inline fun <T, E: Exception> Result<T, E>.onError(action: (E) -> Unit): Result<T, E> {
    return when(this) {
        is Result.Failure -> {
            action(error)
            this
        }
        is Result.Success -> this 
    }
}  


