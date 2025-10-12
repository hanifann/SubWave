package com.hanifan.subwave.data.login.repository

import com.hanifan.subwave.core.storage.AppDatabase
import com.hanifan.subwave.domain.login.model.User
import com.hanifan.subwave.domain.login.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : UserRepository {

    init {
        runBlocking(Dispatchers.IO) {
            refreshUser()
        }
    }
    @Volatile private var cachedUser: User? = null
    override suspend fun getCurrentUser() = appDatabase.userDao().getLoggedInUser()

    override suspend fun saveUser(user: User) = withContext(Dispatchers.IO) {
        appDatabase.userDao().insertUser(user)
        cachedUser = user
    }

    override suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        appDatabase.userDao().delete(user)
        cachedUser = null
    }

    override suspend fun getAllUser() = appDatabase.userDao().getAll()

    override suspend fun refreshUser() = withContext(Dispatchers.IO){
        val user = appDatabase.userDao().getLoggedInUser()
        cachedUser = user
    }

    override fun getCachedUser(): User? = cachedUser
}