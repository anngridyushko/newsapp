package com.example.newsapp.util

sealed class ResourceStatus<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResourceStatus<T>(data)
    class Error<T>(message: String?) : ResourceStatus<T>(message = message)
    class Loading<T> : ResourceStatus<T>()
}