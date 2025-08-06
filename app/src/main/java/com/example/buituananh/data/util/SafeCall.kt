package com.example.buituananh.data.util

import retrofit2.Response
import java.nio.channels.UnresolvedAddressException

suspend fun <T : Any> safeCall(
    action: suspend () -> Response<T>,
): Result<T, Exception> {
    return try {
        val response = action.invoke()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.Success(it)
            } ?: Result.Failure(Exception("Unknown error"))
        }
        when (response.code()) {
            400 -> Result.Failure(Exception("400 error"))
            401 -> Result.Failure(Exception("No authentication"))
            403 -> Result.Failure(Exception("No authentication"))
            500 -> Result.Failure(Exception("Server error"))
            else -> Result.Failure(Exception("Unknown error"))
        }
    } catch (e: UnresolvedAddressException) {
        Result.Failure(Exception("No internet connection"))
    } catch (e: Exception) {
        Result.Failure(e)
    }
}
