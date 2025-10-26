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

    BackHandler(enabled = current != Screen.Home.route) {
        Router.navigate(Screen.Home.route)
    }

    Scaffold(
        bottomBar = {
            if (current !in listOf(Screen.Login.route, Screen.Signup.route, Screen.EmailVerify.route,
                    Screen.AccountSetup.route, Screen.PhoneVerify.route)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 5.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    AppNavBar(currentRoute = current, onItemClick = { route -> Router.navigate(route) })
                }
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
                Screen.EmailVerify.route -> EmailVerifyScreen()
                Screen.AccountSetup.route -> AccountSetupScreen()
                Screen.AccountSetupSuccess.route -> AccountSetupSuccessScreen()
                Screen.PhoneVerify.route -> PhoneVerifyScreen()
                Screen.Home.route -> HomeScreen()
                Screen.Marketplace.route -> MarketplaceScreen()
                Screen.Portfolio.route -> PortfolioScreen()
                Screen.Watchlist.route -> WatchlistScreen()
            }
        }
    }
}
