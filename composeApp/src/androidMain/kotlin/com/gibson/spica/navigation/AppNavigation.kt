package com.gibson.spica.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.ui.AppNavBar
import com.gibson.spica.ui.screens.*

@Composable
actual fun AppNavigation() {
    val current = Router.currentRoute

    // ✅ Android back button only (safe inside androidMain)
    BackHandler(enabled = current != Screen.Home.route) {
        Router.navigate(Screen.Home.route)
    }

    Scaffold(
        bottomBar = {
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (current) {
                Screen.Signup.route -> SignupScreen()
                Screen.Login.route -> LoginScreen()
                Screen.AccountSetup.route -> AccountSetupScreen()
                Screen.EmailVerify.route -> EmailVerifyScreen()
                Screen.PhoneVerify.route -> PhoneVerifyScreen()
                Screen.Home.route -> HomeScreen()
                Screen.Marketplace.route -> MarketplaceScreen()
                Screen.Portfolio.route -> PortfolioScreen()
                Screen.Watchlist.route -> WatchlistScreen()
            }
        }
    }
}
