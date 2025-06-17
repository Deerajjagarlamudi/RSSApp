package com.deeraj.rssfeedapp.utils

sealed class Resources<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(message: String?) : Resources<T>(error = message)
    class Loading<T>() : Resources<T>()
    class Empty<T>() : Resources<T>()

}