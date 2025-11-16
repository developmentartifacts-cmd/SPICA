package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * AuthViewModel — updated to include a simple inline OTP handling state.
 * Real phone verification should be implemented in the Android module (PhoneAuthProvider),
 * but this provides the UI-driving state and placeholders to integrate platform code.
 */
class AuthViewModel(
    private val authService: AuthService = AuthService()
) : ViewModel() {

    // shared fields
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var termsAccepted by mutableStateOf(false)

    // phone fields
    var dialCode by mutableStateOf("+234")
    var phoneNumber by mutableStateOf("")
    var otpCode by mutableStateOf("")
    var otpSent by mutableStateOf(false)           // true when code requested and awaiting entry
    var otpCountdown by mutableStateOf(0)          // seconds remaining
    var isLoading by mutableStateOf(false)

    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    // -------------------------
    // Email flows (unchanged)
    // -------------------------
    fun loginEmail(onSuccess: (() -> Unit)? = null) {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password required."
            return
        }
        isLoading = true
        viewModelScope.launch {
            val res = authService.signInEmail(email.trim(), password)
            isLoading = false
            res.onSuccess {
                successMessage = "Login successful!"
                Router.navigate(Screen.Home.route)
                onSuccess?.invoke()
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Login failed"
            }
        }
    }

    fun signupEmail() {
        if (!termsAccepted) {
            errorMessage = "You must accept Terms & Privacy Policy."
            return
        }
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "All fields required."
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match."
            return
        }

        isLoading = true
        viewModelScope.launch {
            val res = authService.signUpEmail(email.trim(), password)
            isLoading = false
            res.onSuccess {
                successMessage = "Signup successful — check your email to verify."
                Router.navigate(Screen.EmailVerify.route)
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Signup failed"
            }
        }
    }

    fun signupWithGoogle(idToken: String) {
        isLoading = true
        viewModelScope.launch {
            val res = authService.signInWithGoogleIdToken(idToken)
            isLoading = false
            res.onSuccess {
                successMessage = "Signed in with Google"
                Router.navigate(Screen.AccountSetup.route)
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Google sign-in failed"
            }
        }
    }

    // -------------------------
    // Phone flows (UI state)
    // -------------------------
    /**
     * Initiates phone flow. In a real app, call the Android PhoneAuthProvider here and set `otpSent`
     * when SMS is sent. For now this method triggers UI state and a local timer; replace with
     * platform-specific code that obtains verificationId and handles callbacks.
     */
    fun startPhoneVerification(fullPhone: String, onSmsRequested: ((Boolean) -> Unit)? = null) {
        if (fullPhone.isBlank()) {
            errorMessage = "Phone number required."
            return
        }
        errorMessage = null
        isLoading = true
        viewModelScope.launch {
            try {
                // Placeholder: in Android, launch PhoneAuthProvider flow and wait callback.
                // For UI demo we mark otpSent true and start countdown.
                delay(600) // simulated network latency
                otpSent = true
                otpCode = ""
                startOtpCountdown()
                successMessage = "OTP sent to $fullPhone"
                onSmsRequested?.invoke(true)
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Failed to request code"
                onSmsRequested?.invoke(false)
            } finally {
                isLoading = false
            }
        }
    }

    private fun startOtpCountdown(seconds: Int = 60) {
        viewModelScope.launch {
            otpCountdown = seconds
            while (otpCountdown > 0) {
                delay(1000)
                otpCountdown -= 1
            }
        }
    }

    fun resendOtp(fullPhone: String) {
        // In real implementation, re-trigger platform phone auth resend
        startPhoneVerification(fullPhone)
    }

    /**
     * Verify OTP entered by user.
     * Replace with platform-specific verification using verificationId + code.
     * For now: accept any 4-8 digit code as success to progress to AccountSetup.
     */
    fun verifyPhoneOtp(onVerified: (() -> Unit)? = null) {
        if (!otpSent) {
            errorMessage = "No OTP requested."
            return
        }
        if (otpCode.length < 4) {
            errorMessage = "Enter the full code."
            return
        }

        isLoading = true
        viewModelScope.launch {
            try {
                // Placeholder verification:
                delay(500)
                // If real implementation, verify with Firebase using verificationId and code.
                successMessage = "Phone verified"
                Router.navigate(Screen.AccountSetup.route)
                onVerified?.invoke()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Verification failed"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }
}
