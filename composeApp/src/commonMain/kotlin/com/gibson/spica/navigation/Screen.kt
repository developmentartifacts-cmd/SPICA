package com.gibson.spica.navigation

/**
 * Represents all navigable routes in SPICA.
 * Used by Router and NavigationModel.
 */
sealed class Screen(val route: String) {
    object Signup : Screen("signup")
    object Login : Screen("login")
    object AccountSetup : Screen("account_setup")
    object AccountSetupSuccess : Screen("account_setup_success")
    object EmailVerify : Screen("email_verify")
    object PhoneVerify : Screen("phone_verify")
    object Home : Screen("home")
    object Marketplace : Screen("marketplace")
    object Portfolio : Screen("portfolio")
    object Watchlist : Screen("watchlist")

    companion object {
        fun fromRoute(route: String?): Screen? = when (route) {
            Signup.route -> Signup
            Login.route -> Login
            AccountSetup.route -> AccountSetup
            AccountSetupSuccess.route -> AccountSetupSuccess
            EmailVerify.route -> EmailVerify
            PhoneVerify.route -> PhoneVerify
            Home.route -> Home
            Marketplace.route -> Marketplace
            Portfolio.route -> Portfolio
            Watchlist.route -> Watchlist
            else -> null
        }
    }
}
