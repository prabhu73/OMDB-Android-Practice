package com.myomdbapplication.repository.api

sealed class ResponseState<T> {

    class Loading<T>: ResponseState<T>()
    data class Success<T>(val data: T): ResponseState<T>()
    data class Failed<T>(val data: String): ResponseState<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(data: String) = Failed<T>(data)
    }
}

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}