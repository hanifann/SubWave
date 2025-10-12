package com.hanifan.subwave.data.login.data_source

import com.hanifan.subwave.core.network.ResponseDto
import com.hanifan.subwave.data.login.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginRemoteDataSource {
    @GET("getUser")
    suspend fun getUser(
        @Query("u") username: String,
        @Query("p") password: String
    ): ResponseDto<UserDto>
}