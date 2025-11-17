package com.gibson.spica.viewmodel

import com.gibson.spica.data.repository.AuthRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 💡 1. Immutable State Data Class for UDF
data class EmailVerifyState(
    val isLoading: Boolean = false,
    val message: String? = null,
    val isVerified: Boolean = false,
    val navigationTarget: EmailVerifyNavigationTarget? = null // New
)

// 💡 Sealed class for clean navigation routing
sealed class EmailVerifyNavigationTarget {
    data object AccountSetup : EmailVerifyNavigationTarget()
}

// 💡 2. Use the KMP ViewModel base and inject the AuthRepository
class EmailVerifyViewModel(
    private val authRepository: AuthRepository // 💡 Injected via Koin
) : ViewModel() { // 💡 KMP-safe ViewModel base

    private val _state = MutableStateFlow(EmailVerifyState())
    val state: StateFlow<EmailVerifyState> = _state.asStateFlow()

    init {
        // Automatically send verification email on entry if needed
        viewModelScope.launch {
            sendVerificationIfNeeded()
        }
    }

    private fun updateState(block: (EmailVerifyState) -> EmailVerifyState) {
        _state.value = block(_state.value)
    }

    // Call this from the UI when the navigation is handled
    fun navigationHandled() = updateState { it.copy(navigationTarget = null) }

    private suspend fun sendVerificationIfNeeded() {
        // We rely on the AuthRepository to know the current user's state
        val user = authRepository.currentUser.value
        if (user != null && !user.isEmailVerified) {
            val res = authRepository.sendEmailVerification()
            updateState {
                it.copy(
                    message = res.exceptionOrNull()?.localizedMessage ?: "Verification email sent."
                )
            }
        }
    }

    fun resendVerificationEmail() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, message = null) }
            val res = authRepository.sendEmailVerification()
            updateState {
                it.copy(
                    isLoading = false,
                    message = res.exceptionOrNull()?.localizedMessage ?: "Verification email resent."
                )
            }
        }
    }

    fun checkVerificationStatus() {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, message = null) }
            
            // 💡 CRITICAL: Reloads the user state from the service layer
            val reloadRes = authRepository.reloadUser() 
            
            if (reloadRes.isFailure) {
                updateState { it.copy(isLoading = false, message = reloadRes.exceptionOrNull()?.localizedMessage) }
                return@launch
            }
            
            // Check the new state from the repository
            val user = authRepository.currentUser.value 
            
            if (user?.isEmailVerified == true) {
                updateState { 
                    it.copy(
                        isLoading = false, 
                        isVerified = true, 
                        message = "Email verified", 
                        navigationTarget = EmailVerifyNavigationTarget.AccountSetup // 💡 Emit Nav Event
                    ) 
                }
            } else {
                updateState { it.copy(isLoading = false, message = "Email not verified yet.") }
            }
        }
    }

    // 💡 Navigation logic moved to state emission
    fun skipVerification() {
        updateState { it.copy(navigationTarget = EmailVerifyNavigationTarget.AccountSetup) }
    }
}
