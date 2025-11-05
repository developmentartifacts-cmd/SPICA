package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.ui.AppNavBar
import com.gibson.spica.ui.screens.*
import com.gibson.spica.viewmodel.AccountSetupViewModel

@Composable
actual fun AppNavigation() {
    val current = Router.currentRoute
    val sharedAccountSetupViewModel = remember { AccountSetupViewModel() }

    // 🔙 Back button handling
    BackHandler(enabled = current != Screen.Home.route) {
        if (current !in listOf(
                Screen.Login.route,
                Screen.Signup.route,
                Screen.Welcome.route,
                Screen.EmailVerify.route,
                Screen.AccountSetup.route
            )
        ) {
            Router.navigate(Screen.Home.route)
        }
    }

    // 🧭 Scaffold layout
    Scaffold(
        bottomBar = {
            if (current !in listOf(
                    Screen.Welcome.route,
                    Screen.Login.route,
                    Screen.Signup.route,
                    Screen.EmailVerify.route,
                    Screen.AccountSetup.route
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 5.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AppNavBar(
                        currentRoute = current,
                        onItemClick = { route -> Router.navigate(route) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (current) {
                // 🔹 Startup & Auth
                Screen.Welcome.route -> WelcomeScreen()
                Screen.Signup.route -> SignupScreen()
                Screen.Login.route -> LoginScreen()
                Screen.EmailVerify.route -> EmailVerifyScreen()
                Screen.AccountSetup.route -> AccountSetupScreen(viewModel = sharedAccountSetupViewModel)

                // 🌍 Main Screens
                Screen.Home.route -> HomeScreen()
                Screen.Marketplace.route -> MarketplaceScreen()
                Screen.Portfolio.route -> PortfolioScreen()
                Screen.Watchlist.route -> WatchlistScreen()

                // 🌀 Fallback if route missing
                else -> CircularProgressIndicator()
            }
        }
    }
}
