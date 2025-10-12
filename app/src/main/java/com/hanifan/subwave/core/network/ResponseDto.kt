package com.hanifan.subwave.core.network

import com.hanifan.subwave.common.Constant
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseDto<T> (
    @param:Json(name = Constant.SUBSONIC_ROOT_KEY)
    val response: ResponseDetailsDto<T>
)

@JsonClass(generateAdapter = true)
data class ResponseDetailsDto<T>(
    val status: String,
    val version: String,
    val type: String,
    val serverVersion: String,
    val data: T? = null,
    val error: ErrorResponseDto? = null
)

@JsonClass(generateAdapter = true)
data class ErrorResponseDto (
    val code: String,
    val message: String
)

fun <T> ResponseDto<T>.toResponse(): Response<T> = Response(response.toResponseDetails())

fun <T> ResponseDetailsDto<T>.toResponseDetails(): ResponseDetails<T> = ResponseDetails(
        status,
        version,
        type,
        serverVersion,
        data,
        error?.toErrorResponse()
    )

fun ErrorResponseDto.toErrorResponse(): ErrorResponse = ErrorResponse(
        code,
        message
    )
