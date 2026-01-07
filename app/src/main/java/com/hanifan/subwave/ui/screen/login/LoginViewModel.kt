package com.hanifan.subwave.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hanifan.subwave.common.Resource
import com.hanifan.subwave.domain.login.model.User
import com.hanifan.subwave.domain.login.repository.LoginRepository
import com.hanifan.subwave.domain.login.repository.UserRepository
import com.hanifan.subwave.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState>
        get() = _loginState.asStateFlow()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState>
        get() = _authState.asStateFlow()

    val options = listOf("http", "https")
    private val _httpScheme = MutableStateFlow(options.first())
    val httpScheme: StateFlow<String>
        get() = _httpScheme.asStateFlow()

    private val _isExpanded = MutableStateFlow(false)
    val isExpanded: StateFlow<Boolean>
        get() = _isExpanded.asStateFlow()

    fun updateUsername(username: String) = _loginState.update { it.copy(username = username) }
    fun updatePassword(password: String) = _loginState.update { it.copy(password = password) }
    fun updateUrl(url: String) = _loginState.update { it.copy(url = url) }

    fun updateSelectedItem(item: String) {
        _httpScheme.value = item
        _isExpanded.value = false
    }

    fun setExpanded(expanded: Boolean) {
        _isExpanded.value = expanded
    }

    fun resetAuthState() {
        _authState.value = AuthState()
    }

    fun login(onSuccess: (route: Routes) -> Unit) {
        viewModelScope.launch {
            userRepository.refreshUser()
            repository.authentication(
                username = loginState.value.username,
                password = loginState.value.password,
                baseUrl = "${httpScheme.value}://${loginState.value.url}",
            ).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        handleLoginSuccess(result.data)
                        onSuccess(Routes.HomeRoute)
                    }
                    is Resource.Error -> {
                        handleLoginFailed(result.message)
                    }
                    is Resource.Loading -> {
                        _authState.value = _authState.value.copy(
                            isLoading = true
                        )
                    }
                }
            }
        }
    }
    private suspend fun handleLoginSuccess(user: User?) {
        if (user == null) {
            _authState.value = _authState.value.copy(
                isLoading = false,
                errorMessage = "User data is missing"
            )
        }

        userRepository.saveUser(
            // use force unwrap becasuse null already checked above
            user!!.copy(
                serverUrl = "${httpScheme.value}://${loginState.value.url}",
                isLoggedIn = true,
                username = loginState.value.username
            )
        )
        _authState.value = _authState.value.copy(
            isAuthenticated = true,
            isLoading = false,
            errorMessage = null
        )
    }

    private suspend fun handleLoginFailed(message: String?) {
        userRepository.deleteUser(
            User(
                username = loginState.value.username,
                scrobblingEnabled = false,
                adminRole = false,
                downloadRole = false,
                streamRole = false,
                isLoggedIn = true,
                serverUrl = "${httpScheme.value}://${loginState.value.url}",
            )
        )
        _authState.value = _authState.value.copy(
            isLoading = false,
            errorMessage = message
        )
    }
}