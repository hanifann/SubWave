package com.hanifan.subwave.domain.login.repository

import com.hanifan.subwave.common.Resource
import com.hanifan.subwave.domain.login.model.User
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun authentication(
        username: String,
        password: String
    ): Flow<Resource<User>>
}