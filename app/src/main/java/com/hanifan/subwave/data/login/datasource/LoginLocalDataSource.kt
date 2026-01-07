package com.hanifan.subwave.data.login.datasource

import com.hanifan.subwave.core.storage.AppDatabase
import com.hanifan.subwave.domain.login.model.User
import javax.inject.Inject

interface LoginLocalDataSource {
    fun getUser(username: String): User?
    fun insertUser(user: User)
    fun deleteUser(user: User)
}

class LoginLocalDataSourceImpl @Inject constructor (
    private val database: AppDatabase
) : LoginLocalDataSource {
    override fun getUser(username: String): User? {
        return database.userDao().findByUsername(username)
    }

    override fun insertUser(user: User) {
        database.userDao().insertUser(user)
    }

    override fun deleteUser(user: User) {
        database.userDao().delete(user)
    }

}