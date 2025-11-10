package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import kotlinx.coroutines.launch

data class EmailVerifyState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isVerified: Boolean = false
)

class EmailVerifyViewModel(
    private val authService: AuthService = AuthService()
) : ViewModel() {

    var state by mutableStateOf(EmailVerifyState())
        private set

    init {
        // send verification if needed (safe to call)
        viewModelScope.launch {
            sendVerificationIfNeeded()
        }
    }

    private suspend fun sendVerificationIfNeeded() {
        val user = authService.currentUser
        if (user != null && !user.isEmailVerified) {
            val res = authService.sendEmailVerification()
            state = state.copy(message = res.exceptionOrNull()?.localizedMessage ?: "Verification email sent.")
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, message = null)
            val res = authService.sendEmailVerification()
            state = state.copy(isLoading = false, message = res.exceptionOrNull()?.localizedMessage ?: "Verification email resent.")
        }
    }

    fun checkVerificationStatus() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, message = null)
            val reloadRes = authService.reloadCurrentUser()
            if (reloadRes.isFailure) {
                state = state.copy(isLoading = false, message = reloadRes.exceptionOrNull()?.localizedMessage)
                return@launch
            }
            val user = reloadRes.getOrNull()
            if (user?.isEmailVerified == true) {
                state = state.copy(isLoading = false, isVerified = true, message = "Email verified")
                Router.navigate(Screen.AccountSetup.route)
            } else {
                state = state.copy(isLoading = false, message = "Email not verified yet.")
            }
        }
    }

    fun skipVerification() {
        Router.navigate(Screen.AccountSetup.route)
    }
}
