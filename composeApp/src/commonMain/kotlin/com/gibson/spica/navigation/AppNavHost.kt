package com.gibson.spica.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.gibson.spica.ui.screens.*

@Composable
fun AppNavHost(
    startDestination: Route = Route.Welcome
) {
    val navigator = remember { Navigator(startDestination) }

    BackHandler(enabled = navigator.canGoBack()) {
        navigator.goBack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (navigator.currentRoute()) {

            // AUTH FLOW
            Route.Welcome -> WelcomeScreen { navigator.navigate(Route.Login) }
            Route.Signup -> SignupScreen { navigator.goBack() }
            Route.Login -> LoginScreen(
                onSignup = { navigator.navigate(Route.Signup) },
                onLoginSuccess = { navigator.navigateClear(Route.Home) }
            )
            Route.EmailVerification -> EmailVerificationScreen { navigator.goBack() }
            Route.AccountSetup -> AccountSetupScreen { navigator.navigateClear(Route.Home) }

            // MAIN GRAPH (shows bottom bar)
            Route.Home -> HomeScreen(navigator)
            Route.Exchange -> ExchangeScreen(navigator)
            Route.Portfolio -> PortfolioScreen(navigator)
            Route.Watchlist -> WatchlistScreen(navigator)

            // FULL SCREEN SECTIONS (NO bottom bar)
            Route.Settings -> SettingsScreen { navigator.goBack() }
            Route.AssetDetails -> AssetDetailsScreen { navigator.goBack() }
            Route.PostDetails -> PostDetailsScreen { navigator.goBack() }
            Route.Search -> SearchScreen { navigator.goBack() }
        }
    }
}
