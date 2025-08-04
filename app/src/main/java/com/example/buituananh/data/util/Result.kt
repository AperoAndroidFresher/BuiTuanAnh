package com.example.buituananh.data.util

sealed interface Result<out T, out E: Exception> {
    data class Success<out T>(val data: T) : Result<T, Nothing>
    data class Failure<out E: Exception>(val error: E) : Result<Nothing, E>
}
