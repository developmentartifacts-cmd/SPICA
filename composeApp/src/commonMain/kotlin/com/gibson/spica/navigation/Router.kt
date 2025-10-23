package com.gibson.spica.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object Router {
    var currentRoute: String? by mutableStateOf(Screen.Login.route)
        private set

    fun navigate(route: String) {
        currentRoute = route
    }

    fun resetTo(route: String) {
        currentRoute = route
    }
}
