package com.project.veganlife.data.model

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val errorCode: String, val description: String) : ApiResult<Nothing>()
    data class Exception(val e: Throwable) : ApiResult<Nothing>()
}