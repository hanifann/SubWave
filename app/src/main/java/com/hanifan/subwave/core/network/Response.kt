package com.hanifan.subwave.core.network

data class Response<T> (
    val response: ResponseDetails<T>
)

data class ResponseDetails<T>(
    val status: String,
    val version: String,
    val type: String,
    val serverVersion: String,
    val data: T? =null,
    val error: ErrorResponse? = null
)

data class ErrorResponse (
    val code: String,
    val message: String
)