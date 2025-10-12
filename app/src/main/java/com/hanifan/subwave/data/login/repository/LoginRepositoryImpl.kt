package com.hanifan.subwave.data.login.repository

import com.hanifan.subwave.common.Resource
import com.hanifan.subwave.core.network.safeApiCall
import com.hanifan.subwave.data.login.data_source.LoginRemoteDataSource
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
    ): Flow<Resource<User>> = flow {
        emit(Resource.Loading())

        emit(
            safeApiCall(
                call = { loginRemoteDataSource.getUser(username, password) },
                mapper = {
                    it.toUser()
                }
            )
        )
    }
}