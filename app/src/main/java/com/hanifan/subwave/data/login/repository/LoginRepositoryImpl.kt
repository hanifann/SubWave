package com.hanifan.subwave.data.login.repository

import com.hanifan.subwave.common.Resource
import com.hanifan.subwave.core.network.safeApiCall
import com.hanifan.subwave.data.login.datasource.LoginRemoteDataSource
import com.hanifan.subwave.data.login.dto.toUser
import com.hanifan.subwave.domain.login.model.User
import com.hanifan.subwave.domain.login.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginRemoteDataSource: LoginRemoteDataSource,
): LoginRepository {
    override suspend fun authentication(
        username: String,
        password: String,
        baseUrl: String
    ): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        val response = loginRemoteDataSource.getUser(
            username = username,
            password = password,
            url = "${baseUrl}/getUser"
        )

        if(!response.isSuccessful) {
            emit(Resource.Error(response.message()))
        }

        val userDto = response.body() ?: run {
            emit(Resource.Error("User not found"))
            return@flow
        }

        val url = response.raw().request.url
        val salt = url.queryParameter("s") ?: ""
        val token = url.queryParameter("t") ?: ""

        val updatedData = userDto.copy(
            response = userDto.response.copy(
                data = userDto.response.data?.copy(
                    salt = salt,
                    token = token
                )
            )
        )

        emit(
            safeApiCall(
                call = { updatedData },
                mapper = { it.toUser() }
            )
        )
    }
}