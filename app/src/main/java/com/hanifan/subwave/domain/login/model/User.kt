package com.hanifan.subwave.domain.login.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey val username: String,
    val scrobblingEnabled: Boolean,
    val adminRole: Boolean,
    val downloadRole: Boolean,
    val streamRole: Boolean,
    val isLoggedIn: Boolean = false,
    val serverUrl: String = "",
    val token: String = "",
    val salt: String = ""
)
