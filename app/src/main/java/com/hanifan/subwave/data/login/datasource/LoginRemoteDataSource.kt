package com.hanifan.subwave.data.login.datasource

import com.hanifan.subwave.core.network.ResponseDto
import com.hanifan.subwave.data.login.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface LoginRemoteDataSource {
    @GET()
    suspend fun getUser(
        @Url url: String,
        @Query("u") username: String,
        @Query("p") password: String,
    ): Response<ResponseDto<UserDto>>
}