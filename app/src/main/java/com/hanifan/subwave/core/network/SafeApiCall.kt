package com.hanifan.subwave.core.network

import com.hanifan.subwave.common.Resource
import com.hanifan.subwave.utils.extension.ensureError
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <T, R> safeApiCall(
    crossinline call: suspend () -> ResponseDto<T>,
    crossinline mapper: (T) -> R,
): Resource<R> {
    return try {
        val dto = call()
        val base = dto.toResponse().response

        base.error?.let { return Resource.Error(it.message) }

        val data = base.data ?: return Resource.Error("Empty data")

        Resource.Success(mapper(data))
    } catch (e: HttpException) {
        Resource.Error(message = "HTTP Error: ${e.code()}")
    } catch (_: IOException) {
        Resource.Error(message = "Network Error: Check connection.")
    } catch (e: Exception) {
        Resource.Error(message = e.localizedMessage.ensureError())

    }
}