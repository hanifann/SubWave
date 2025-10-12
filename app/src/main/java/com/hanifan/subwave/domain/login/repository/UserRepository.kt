package com.hanifan.subwave.domain.login.repository

import com.hanifan.subwave.domain.login.model.User

interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun saveUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun refreshUser()
    suspend fun getAllUser(): List<User>
    fun getCachedUser(): User?
}