package com.gibson.spica.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

// -------------------------------------------------------------
// MULTIPLATFORM BACK HANDLER
// -------------------------------------------------------------
expect fun BackHandler(
    enabled: Boolean = true,
    onBack: () -> Unit
)

// -------------------------------------------------------------
// SIMPLE NAVIGATION CONTROLLER
// (production-ready, minimal, multiplatform-safe)
// -------------------------------------------------------------
class Navigator(
    private var current: Route
) {
    private val backStack = ArrayDeque<Route>()

    fun currentRoute(): Route = current

    fun navigate(route: Route) {
        // push current to back stack
        backStack.addLast(current)
        current = route
    }

    fun navigateClear(route: Route) {
        // used after login or onboarding (no back allowed)
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

// -------------------------------------------------------------
// REMEMBER NAVIGATOR (Compose helper)
// -------------------------------------------------------------
@Composable
fun rememberNavigator(start: Route): Navigator {
    return remember { Navigator(start) }
}
