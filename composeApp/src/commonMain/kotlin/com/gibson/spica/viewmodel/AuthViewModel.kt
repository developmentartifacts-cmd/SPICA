package com.gibson.spica.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

/**
 * ViewModel for handling authentication logic (signup & login).
 */
class AuthViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun onEmailChange(newValue: String) {
        email = newValue
    }

    fun onPasswordChange(newValue: String) {
        password = newValue
    }

    fun login() {
        isLoading = true
        errorMessage = null
        AuthService.signIn(email, password) { success, error ->
            isLoading = false
            if (success) {
                Router.navigate(Screen.Home.route)
            } else {
                errorMessage = error ?: "Login failed"
            }
        }
    }

    fun signup() {
        isLoading = true
        errorMessage = null
        AuthService.signUp(email, password) { success, error ->
            isLoading = false
            if (success) {
                Router.navigate(Screen.AccountSetup.route)
            } else {
                errorMessage = error ?: "Signup failed"
            }
        }
    }

    fun logout() {
        AuthService.signOut()
        Router.navigate(Screen.Login.route)
    }
}
