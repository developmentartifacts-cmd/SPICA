package com.gibson.spica.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Central navigation controller for SPICA.
 * Works like a lightweight NavHost with Compose state.
 */
object Router {
    // 🟢 Default route: Splash Screen (first screen seen on app open)
    var currentRoute: String by mutableStateOf(Screen.Welcome.route)
        private set

    fun navigate(route: String) {
        currentRoute = route
    }

    /**
     * Used for resetting navigation flow after authentication changes.
     * Example: when logging out or switching user.
     */
    fun resetTo(route: String) {
        currentRoute = route
    }
}
