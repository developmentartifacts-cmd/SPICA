package com.gibson.spica.navigation

/**
 * Represents all navigable routes in SPICA.
 * Used by Router and NavigationModel.
 */
sealed class Screen(val route: String) {

    // 🔐 Authentication Flow
    object Signup : Screen("signup")
    object Login : Screen("login")
    object EmailVerify : Screen("email_verify")
    object AccountSetup : Screen("account_setup")

    // 🏠 Main App Screens
    object Home : Screen("home")
    object Marketplace : Screen("marketplace")
    object Portfolio : Screen("portfolio")
    object Watchlist : Screen("watchlist")

    companion object {
        fun fromRoute(route: String?): Screen? = when (route) {
            Signup.route -> Signup
            Login.route -> Login
            EmailVerify.route -> EmailVerify
            AccountSetup.route -> AccountSetup
            Home.route -> Home
            Marketplace.route -> Marketplace
            Portfolio.route -> Portfolio
            Watchlist.route -> Watchlist
            else -> null
        }
    }
}
