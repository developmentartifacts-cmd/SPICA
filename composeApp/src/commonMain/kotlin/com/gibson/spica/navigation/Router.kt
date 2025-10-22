package com.gibson.spica.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Router {
    var currentRoute: String by mutableStateOf(Screen.Home.route)
        private set

    fun navigate(route: String) {
        if (route != currentRoute) {
            currentRoute = route
        }
    }

    fun resetToHome() {
        currentRoute = Screen.Home.route
    }
}
