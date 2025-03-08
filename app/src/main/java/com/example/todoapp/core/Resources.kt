package com.example.todoapp.core

sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    data class Success<out T>(val data : T) : Resource<T>()
    data class Failure(val exception: Throwable) : Resource<Nothing>()

}