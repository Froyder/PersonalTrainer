package com.froyder.personaltrainer.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.froyder.personaltrainer.data.repository.AuthRepository
import com.froyder.personaltrainer.data.repository.LocalRepository
import com.froyder.personaltrainer.utils.CrashReporter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository = AuthRepository(),
    private val localRepository: LocalRepository? = null
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    private val _isGuestMode = MutableStateFlow(
        localRepository?.isGuestMode().also {
        } ?: false
    )
    val isGuestMode = _isGuestMode.asStateFlow()

    val isLoggedIn: Boolean get() = authRepository.currentUser != null
    val currentUserId: String get() = authRepository.currentUser?.uid ?:
                                        if (_isGuestMode.value) "guest_local" else ""

    val authStateFlow = authRepository.authStateFlow

    fun continueAsGuest() {
        _isGuestMode.value = true
        localRepository?.setGuestMode(true)
    }

    fun exitGuestMode() {
        _isGuestMode.value = false
        localRepository?.clearGuestMode()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                authRepository.login(email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                CrashReporter.recordException(e)
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                authRepository.register(email, password)
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                CrashReporter.recordException(e)
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState.Idle
        }
    }

    fun deleteAccount(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                authRepository.deleteAccount()
                onComplete()
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to delete account")
            }
        }
    }

    fun reset() {
        _authState.value = AuthState.Idle
        _isGuestMode.value = false
        localRepository?.clearGuestMode()
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}