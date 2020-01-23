package com.carloscar.themoviedb.data.network.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class NetworkUtil {

    sealed class ResultWrapper<out T> {
        data class Success<out T>(val value: T) : ResultWrapper<T>()
        data class GenericError(val code: Int? = null, val error: String? = null) :
            ResultWrapper<Nothing>()

        object NetworkError : ResultWrapper<Nothing>()
    }

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val errorResponse = throwable.toString()
                        ResultWrapper.GenericError(code, errorResponse)
                    }
                    else -> {
                        ResultWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }
}