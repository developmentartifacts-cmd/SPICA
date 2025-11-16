package com.gibson.spica.viewmodel

import com.gibson.spica.data.repository.AuthRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 💡 1. Immutable State Data Class for UDF
data class AuthState(
    // Shared fields
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsAccepted: Boolean = false,
    
    // Phone fields
    val dialCode: String = "+234",
    val phoneNumber: String = "",
    val otpCode: String = "",
    val otpSent: Boolean = false,
    val otpCountdown: Int = 0,
    
    // UI Feedback
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    
    // 💡 Navigation Event (New KMP approach: ViewModel emits route, screen acts on it)
    val navigationTarget: AuthNavigationTarget? = null
)

// 💡 Sealed class for clean navigation routing from the ViewModel
sealed class AuthNavigationTarget {
    data object Home : AuthNavigationTarget()
    data object EmailVerify : AuthNavigationTarget()
    data object AccountSetup : AuthNavigationTarget()
}


// 💡 2. Use the KMP ViewModel base and inject dependencies
class AuthViewModel(
    private val authRepository: AuthRepository // 💡 Injected via Koin
) : ViewModel() { // 💡 KMP-safe ViewModel base

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    // --- State Updaters ---
    fun setEmail(value: String) = updateState { it.copy(email = value, errorMessage = null) }
    fun setPassword(value: String) = updateState { it.copy(password = value, errorMessage = null) }
    fun setConfirmPassword(value: String) = updateState { it.copy(confirmPassword = value, errorMessage = null) }
    fun setTermsAccepted(value: Boolean) = updateState { it.copy(termsAccepted = value) }
    fun setPhoneNumber(value: String) = updateState { it.copy(phoneNumber = value, errorMessage = null) }
    fun setOtpCode(value: String) = updateState { it.copy(otpCode = value, errorMessage = null) }

    private fun updateState(block: (AuthState) -> AuthState) {
        _state.value = block(_state.value)
    }

    // Call this from the UI when the navigation is handled
    fun navigationHandled() = updateState { it.copy(navigationTarget = null) }

    // -------------------------
    // Email flows
    // -------------------------
    fun loginEmail() {
        if (_state.value.email.isBlank() || _state.value.password.isBlank()) {
            return updateState { it.copy(errorMessage = "Email and password required.") }
        }
        updateState { it.copy(isLoading = true, errorMessage = null, successMessage = null) }

        viewModelScope.launch {
            val currentState = _state.value
            val res = authRepository.signInEmail(currentState.email.trim(), currentState.password)
            
            res.onSuccess {
                updateState {
                    it.copy(
                        successMessage = "Login successful!",
                        navigationTarget = AuthNavigationTarget.Home,
                        isLoading = false
                    )
                }
            }.onFailure {
                updateState {
                    it.copy(
                        errorMessage = it.localizedMessage ?: "Login failed",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun signupEmail() {
        val currentState = _state.value
        if (!currentState.termsAccepted) {
            return updateState { it.copy(errorMessage = "You must accept Terms & Privacy Policy.") }
        }
        if (currentState.password != currentState.confirmPassword) {
            return updateState { it.copy(errorMessage = "Passwords do not match.") }
        }

        updateState { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
        
        viewModelScope.launch {
            val res = authRepository.signUpEmail(currentState.email.trim(), currentState.password)
            
            res.onSuccess {
                updateState {
                    it.copy(
                        successMessage = "Signup successful — check your email to verify.",
                        navigationTarget = AuthNavigationTarget.EmailVerify,
                        isLoading = false
                    )
                }
            }.onFailure {
                updateState {
                    it.copy(
                        errorMessage = it.localizedMessage ?: "Signup failed",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun signupWithGoogle(idToken: String) {
        updateState { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
        
        viewModelScope.launch {
            val res = authRepository.signInWithGoogleToken(idToken)
            
            res.onSuccess {
                updateState {
                    it.copy(
                        successMessage = "Signed in with Google",
                        navigationTarget = AuthNavigationTarget.AccountSetup,
                        isLoading = false
                    )
                }
            }.onFailure {
                updateState {
                    it.copy(
                        errorMessage = it.localizedMessage ?: "Google sign-in failed",
                        isLoading = false
                    )
                }
            }
        }
    }

    // -------------------------
    // Phone flows (Cleaned-up state management)
    // -------------------------
    fun startPhoneVerification(onSmsRequested: (Boolean) -> Unit) {
        val fullPhone = _state.value.dialCode + _state.value.phoneNumber
        if (fullPhone.isBlank()) {
            return updateState { it.copy(errorMessage = "Phone number required.") }
        }
        
        // 💡 In a real implementation, you would call the platform-specific PhoneAuthRepository here.
        // The implementation details are abstracted away, but the UI state logic remains here.
        updateState { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
        
        viewModelScope.launch {
            try {
                // Placeholder: Simulate request and platform-specific SMS logic completion
                delay(600) 
                updateState { 
                    it.copy(
                        otpSent = true, 
                        otpCode = "", 
                        successMessage = "OTP sent to $fullPhone"
                    ) 
                }
                startOtpCountdown()
                onSmsRequested.invoke(true)
            } catch (e: Exception) {
                updateState { it.copy(errorMessage = e.localizedMessage ?: "Failed to request code") }
                onSmsRequested.invoke(false)
            } finally {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    private fun startOtpCountdown(seconds: Int = 60) {
        viewModelScope.launch {
            updateState { it.copy(otpCountdown = seconds) }
            while (_state.value.otpCountdown > 0 && _state.value.otpSent) {
                delay(1000)
                updateState { it.copy(otpCountdown = it.otpCountdown - 1) }
            }
        }
    }

    fun resendOtp() {
        startPhoneVerification { /* ignore result */ }
    }

    fun verifyPhoneOtp() {
        if (!_state.value.otpSent || _state.value.otpCode.length < 4) {
            return updateState { it.copy(errorMessage = "Enter the full code.") }
        }

        updateState { it.copy(isLoading = true, errorMessage = null, successMessage = null) }
        
        viewModelScope.launch {
            try {
                // Placeholder verification: in a real app, call platform-specific auth.
                delay(500)
                updateState { 
                    it.copy(
                        successMessage = "Phone verified",
                        navigationTarget = AuthNavigationTarget.AccountSetup,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                updateState { it.copy(errorMessage = e.localizedMessage ?: "Verification failed", isLoading = false) }
            }
        }
    }
}
