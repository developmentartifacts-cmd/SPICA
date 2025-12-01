package com.gibson.spica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

// MULTIPLATFORM BACK HANDLER
expect fun BackHandler(enabled: Boolean = true, onBack: () -> Unit)

// SIMPLE NAVIGATION CONTROLLER
class Navigator(
    private var current: Route
) {
    private val backStack = ArrayDeque<Route>()

    fun currentRoute(): Route = current

    fun navigate(route: Route) {
        backStack.addLast(current)
        current = route
    }

    fun navigateClear(route: Route) {
        backStack.clear()
        current = route
    }

    fun goBack() {
        if (backStack.isNotEmpty()) {
            current = backStack.removeLast()
        }
    }

    fun canGoBack(): Boolean = backStack.isNotEmpty()
}
