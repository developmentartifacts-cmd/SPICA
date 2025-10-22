package com.gibson.spica.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gibson.spica.data.AuthService

/**
 * Simple navigation handler without platform dependency.
 */
object Router {
    // ✅ Check Firebase Auth to decide start screen
    private val initialRoute: String = if (AuthService.getCurrentUser() != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    // ✅ Current route
    var currentRoute: String? by mutableStateOf(initialRoute)
        private set

    fun navigate(route: String) {
        currentRoute = route
    }

    fun resetTo(route: String) {
        currentRoute = route
    }
}
