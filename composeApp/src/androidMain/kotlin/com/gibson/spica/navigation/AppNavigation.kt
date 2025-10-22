package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.data.AuthService
import com.gibson.spica.ui.AppNavBar
import com.gibson.spica.ui.screens.*

/**
 * Android actual implementation of AppNavigation.
 * - Hides bottom nav when unauthenticated (Login/Signup)
 * - Shows bottom nav only when user is logged in
 * - Handles Android back button
 */
@Composable
actual fun AppNavigation() {
    val current = Router.currentRoute
    val user = AuthService.getCurrentUser()

    // ✅ Android back button: only active when not on Home
    BackHandler(enabled = current != Screen.Home.route && user != null) {
        Router.navigate(Screen.Home.route)
    }

    // ✅ Authenticated layout uses Scaffold (bottom nav)
    if (user != null) {
        Scaffold(
            bottomBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AppNavBar(
                        currentRoute = current,
                        onItemClick = { route -> Router.navigate(route) }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                when (current) {
                    Screen.Home.route -> HomeScreen()
                    Screen.Marketplace.route -> MarketplaceScreen()
                    Screen.Portfolio.route -> PortfolioScreen()
                    Screen.Watchlist.route -> WatchlistScreen()
                    else -> HomeScreen()
                }
            }
        }
    } else {
        // ✅ Unauthenticated screens: full screen, no nav bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when (current) {
                Screen.Signup.route -> SignupScreen()
                else -> LoginScreen() // default unauthenticated
            }
        }
    }
}
