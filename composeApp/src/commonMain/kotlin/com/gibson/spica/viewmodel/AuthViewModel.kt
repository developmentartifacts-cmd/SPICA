package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authService: AuthService = AuthService()
) : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var phone by mutableStateOf("")
    var selectedDialCode by mutableStateOf("+234")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)
    var termsAccepted by mutableStateOf(false)

    fun loginEmail() {
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
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Login failed"
            }
        }
    }

    fun signupEmail() {
        if (!termsAccepted) {
            errorMessage = "You must agree to Terms and Privacy Policy."
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
            res.onSuccess { user ->
                successMessage = "Signup successful! Check your email to verify."
                Router.navigate(Screen.EmailVerify.route)
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Signup failed"
            }
        }
    }

    fun signupWithPhone(/* additional params if implementing phone flow */) {
        // TODO: implement phone sign-up launch logic (PhoneAuthProvider flows require callbacks
        // and Activity context for sending/receiving SMS). For Compose Multiplatform,
        // implement platform-specific phone auth glue in Android module and call into here.
        errorMessage = "Phone signup not implemented in shared module (platform-specific)."
    }

    fun signupWithGoogle(idToken: String) {
        // Call this after you obtain an ID token from platform Google sign-in flow (Android)
        isLoading = true
        viewModelScope.launch {
            val res = authService.signInWithGoogleIdToken(idToken)
            isLoading = false
            res.onSuccess {
                successMessage = "Signed in with Google"
                Router.navigate(Screen.Home.route)
            }.onFailure {
                errorMessage = it.localizedMessage ?: "Google sign-in failed"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.signOut()
            Router.resetTo(Screen.Login.route)
        }
    }
}
