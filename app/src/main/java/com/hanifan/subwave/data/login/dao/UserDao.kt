package com.hanifan.subwave.data.login.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.hanifan.subwave.domain.login.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>
    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    fun findByUsername(username: String): User
    @Upsert
    fun insertUser(user: User)
    @Delete
    fun delete(user: User)
    @Query("SELECT * FROM user WHERE isLoggedIn = 1 LIMIT 1")
    fun getLoggedInUser(): User?
}