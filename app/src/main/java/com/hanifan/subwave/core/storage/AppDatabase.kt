package com.hanifan.subwave.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hanifan.subwave.data.login.dao.UserDao
import com.hanifan.subwave.domain.login.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}