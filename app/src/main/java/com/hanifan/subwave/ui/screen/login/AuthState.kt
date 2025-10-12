package com.hanifan.subwave.ui.screen.login

data class AuthState (
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)