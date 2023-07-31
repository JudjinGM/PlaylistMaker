package com.example.playlistmaker.search.data.model

sealed class Resource<T>(val data: T? = null, val errorMessage: ErrorStatusData?= null) {

    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(error: ErrorStatusData) : Resource<T>(errorMessage = error)
}


