package com.hanifan.subwave.data.login.dto

import com.hanifan.subwave.domain.login.model.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val username: String,
    val scrobblingEnabled: Boolean,
    val adminRole: Boolean,
    val downloadRole: Boolean,
    val streamRole: Boolean,
)

fun UserDto.toUser(): User = User(
        username = username,
        scrobblingEnabled =  scrobblingEnabled,
        adminRole =  adminRole,
        downloadRole =  downloadRole,
        streamRole =  streamRole
    )