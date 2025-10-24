package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

data class EmailVerifyState(
    val isLoading: Boolean = false,
    val message: String? = null
)

class EmailVerifyViewModel : ViewModel() {

    var state by mutableStateOf(EmailVerifyState())
        private set

    private val auth = FirebaseAuth.getInstance()

    init {
        sendVerificationEmailIfNeeded()
    }

    private fun sendVerificationEmailIfNeeded() {
        val user = auth.currentUser
        if (user != null && !user.isEmailVerified) {
            user.sendEmailVerification()
                .addOnSuccessListener {
                    state = state.copy(message = "Verification email sent successfully!")
                }
                .addOnFailureListener {
                    state = state.copy(message = "Error: ${it.message}")
                }
        }
    }

    fun resendVerificationEmail() {
        val user = auth.currentUser
        if (user == null) {
            state = state.copy(message = "No logged-in user found.")
            return
        }

        state = state.copy(isLoading = true)
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                state = if (task.isSuccessful) {
                    state.copy(isLoading = false, message = "Verification email resent.")
                } else {
                    state.copy(
                        isLoading = false,
                        message = "Failed to resend: ${task.exception?.message}"
                    )
                }
            }
    }

    fun checkVerificationStatus() {
        val user = auth.currentUser
        if (user == null) {
            state = state.copy(message = "User not logged in.")
            return
        }

        state = state.copy(isLoading = true)
        user.reload().addOnCompleteListener { reloadTask ->
            if (reloadTask.isSuccessful) {
                val refreshedUser = auth.currentUser
                if (refreshedUser?.isEmailVerified == true) {
                    state = state.copy(isLoading = false, message = "Email verified successfully!")
                    Router.navigate(Screen.AccountSetup.route)
                } else {
                    state = state.copy(isLoading = false, message = "Email not verified yet.")
                }
            } else {
                state = state.copy(isLoading = false, message = "Error: ${reloadTask.exception?.message}")
            }
        }
    }

    fun skipVerification() {
        Router.navigate(Screen.AccountSetup.route)
    }
}
