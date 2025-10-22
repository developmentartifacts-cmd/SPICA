package com.gibson.spica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gibson.spica.data.AuthService
import com.google.firebase.auth.FirebaseUser

/**
 * ViewModel that manages authentication state and actions for SPICA.
 * Keeps track of the logged-in user, loading states, and errors.
 */
class AuthViewModel : ViewModel() {

    // Firebase user (null if not signed in)
    var user: FirebaseUser? by mutableStateOf(AuthService.getCurrentUser())
        private set

    // Loading indicator for UI
    var loading by mutableStateOf(false)
        private set

    // Authentication error message
    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Sign in user with email and password.
     */
    fun signIn(email: String, password: String) {
        loading = true
        errorMessage = null

        AuthService.signIn(email, password) { success, error ->
            loading = false
            if (success) {
                user = AuthService.getCurrentUser()
            } else {
                errorMessage = error ?: "Sign in failed"
            }
        }
    }

    /**
     * Register new user with email and password.
     */
    fun signUp(email: String, password: String) {
        loading = true
        errorMessage = null

        AuthService.signUp(email, password) { success, error ->
            loading = false
            if (success) {
                user = AuthService.getCurrentUser()
            } else {
                errorMessage = error ?: "Sign up failed"
            }
        }
    }

    /**
     * Sign out from Firebase.
     */
    fun signOut() {
        AuthService.signOut()
        user = null
    }

    /**
     * Refresh current user (for auto-login or token update).
     */
    fun refreshUser() {
        user = AuthService.getCurrentUser()
    }
}
