package com.gibson.spica.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

class AuthViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var successMessage by mutableStateOf<String?>(null)

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Email and password required."
            return
        }
        isLoading = true
        AuthService.signIn(email, password) { success, error ->
            isLoading = false
            if (success) {
                successMessage = "Login successful!"
                Router.navigate(Screen.Home.route)
            } else {
                errorMessage = error
            }
        }
    }

    fun signup() {
        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            errorMessage = "All fields required."
            return
        }
        if (password != confirmPassword) {
            errorMessage = "Passwords do not match."
            return
        }

        isLoading = true
        AuthService.signUp(email, password) { success, error ->
            isLoading = false
            if (success) {
                successMessage = "Signup successful! Verify your email."
                Router.navigate(Screen.EmailVerify.route)
            } else {
                errorMessage = error
            }
        }
    }

    fun logout() {
        AuthService.signOut {
            Router.navigate(Screen.Login.route)
        }
    }
}
