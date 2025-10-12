package com.hanifan.subwave.navigation

import androidx.lifecycle.ViewModel
import com.hanifan.subwave.domain.login.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    userRepository: UserRepository
): ViewModel() {
    private val _startDestination = MutableStateFlow(
        if (userRepository.getCachedUser() != null) Routes.HomeRoute else Routes.LoginRoute
    )
    val startDestination: StateFlow<Routes>
        get() = _startDestination.asStateFlow()
}