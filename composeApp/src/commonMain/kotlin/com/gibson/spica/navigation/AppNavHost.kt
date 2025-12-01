package com.gibson.spica.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// MAIN TAB SCREENS
import com.gibson.spica.ui.screens.main.HomeScreen
import com.gibson.spica.ui.screens.main.ExchangeScreen
import com.gibson.spica.ui.screens.main.PortfolioScreen
import com.gibson.spica.ui.screens.main.WatchlistScreen

// AUTH SCREENS
import com.gibson.spica.ui.screens.auth.WelcomeScreen
import com.gibson.spica.ui.screens.auth.SignupScreen
import com.gibson.spica.ui.screens.auth.LoginScreen
import com.gibson.spica.ui.screens.auth.EmailVerificationScreen
import com.gibson.spica.ui.screens.auth.AccountSetupScreen

// FULL SCREEN SCREENS
import com.gibson.spica.ui.screens.full.SettingsScreen
import com.gibson.spica.ui.screens.full.PostDetailsScreen
import com.gibson.spica.ui.screens.full.AssetDetailsScreen
import com.gibson.spica.ui.screens.full.SearchScreen

@Composable
fun AppNavHost(
    startDestination: Route = Route.Welcome
) {
    val navigator = rememberNavigator(startDestination)

    BackHandler(enabled = navigator.canGoBack()) {
        navigator.goBack()
    }

    Surface(modifier = Modifier.fillMaxSize()) {

        when (navigator.currentRoute()) {

            // --------------------------------------------------------
            // AUTH FLOW — full screen, no bottom bar
            // --------------------------------------------------------
            Route.Welcome -> WelcomeScreen(
                onNext = { navigator.navigate(Route.Login) }
            )

            Route.Signup -> SignupScreen(
                onBack = { navigator.goBack() },
                onSignupComplete = { navigator.navigate(Route.EmailVerification) }
            )

            Route.Login -> LoginScreen(
                onSignup = { navigator.navigate(Route.Signup) },
                onLoginSuccess = { navigator.navigateClear(Route.Home) }
            )

            Route.EmailVerification -> EmailVerificationScreen(
                onBack = { navigator.goBack() },
                onVerified = { navigator.navigate(Route.AccountSetup) }
            )

            Route.AccountSetup -> AccountSetupScreen(
                onDone = { navigator.navigateClear(Route.Home) }
            )

            // --------------------------------------------------------
            // MAIN BOTTOM-NAV FLOW
            // --------------------------------------------------------
            Route.Home -> HomeScreen(navigator)

            Route.Exchange -> ExchangeScreen(navigator)

            Route.Portfolio -> PortfolioScreen(navigator)

            Route.Watchlist -> WatchlistScreen(navigator)

            // --------------------------------------------------------
            // FULL SCREEN ROUTES (no nav bar)
            // --------------------------------------------------------
            Route.Settings -> SettingsScreen(
                onBack = { navigator.goBack() }
            )

            Route.PostDetails -> PostDetailsScreen(
                onBack = { navigator.goBack() }
            )

            Route.AssetDetails -> AssetDetailsScreen(
                onBack = { navigator.goBack() }
            )

            Route.Search -> SearchScreen(
                onBack = { navigator.goBack() }
            )
        }
    }
}
