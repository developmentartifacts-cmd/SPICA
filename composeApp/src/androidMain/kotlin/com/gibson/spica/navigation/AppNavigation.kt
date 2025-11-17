package com.gibson.spica.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gibson.spica.navigation.Screen
import com.gibson.spica.ui.screens.WelcomeScreen // Assuming your screens are here

// 💡 This is the 'actual' implementation of the 'expect' function
@Composable
actual fun AppNavigation(
    initialDestination: String? // 💡 The determined start route from SplashViewModel
) {
    // We use Screen.Login.route as a failsafe default
    val startDestination = initialDestination ?: Screen.Login.route
    
    // 1. Initialize the NavController
    val navController = rememberNavController()
    
    // 2. Define a simple, consistent navigation function
    val navigate: (String) -> Unit = { route -> 
        navController.navigate(route) {
            // Optional: Pop up to the start destination to avoid a large back stack
            // popUpTo(startDestination) { saveState = true } 
            launchSingleTop = true
            restoreState = true
        }
    }
    
    // 3. Define the NavHost using the determined startDestination
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        // --- Startup & Auth Flow ---
        
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onNavigateToSignup = { navigate(Screen.Signup.route) },
                onNavigateToLogin = { navigate(Screen.Login.route) }
            )
        }
        
        // TODO: Implement LoginScreen, SignupScreen, etc.
        composable(Screen.Login.route) { 
            /* LoginScreen (e.g., uses koinViewModel<AuthViewModel> and passes actions) */
        }
        
        composable(Screen.Signup.route) { 
            /* SignupScreen */
        }

        composable(Screen.EmailVerify.route) {
            /* EmailVerifyScreen */
        }

        composable(Screen.AccountSetup.route) {
            /* AccountSetupScreen */
        }

        // --- Main App Screens (Behind the MainNavHost) ---
        // 💡 You would typically nest a NavGraph here for the main tabs.
        composable(Screen.Home.route) {
            // Placeholder: MainContent(onNavigateToMarketplace = { navigate(Screen.Marketplace.route) })
        }
        
        composable(Screen.Marketplace.route) { 
            // MarketplaceContent
        }

        composable(Screen.Portfolio.route) {
            // PortfolioContent
        }

        composable(Screen.Watchlist.route) {
            // WatchlistContent
        }
    }
}
