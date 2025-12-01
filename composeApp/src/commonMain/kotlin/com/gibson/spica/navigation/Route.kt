package com.gibson.spica.navigation

sealed class Route(val path: String) {

    // AUTH GRAPH
    data object Welcome : Route("welcome")
    data object Signup : Route("signup")
    data object Login : Route("login")
    data object EmailVerification : Route("email_verification")
    data object AccountSetup : Route("account_setup")

    // MAIN GRAPH WITH BOTTOM NAVIGATION
    data object Home : Route("home")
    data object Exchange : Route("exchange") // (Marketplace replaced)
    data object Portfolio : Route("portfolio")
    data object Watchlist : Route("watchlist")

    // FULL SCREEN SUB-ROUTES (no bottom bar)
    data object Settings : Route("settings")
    data object PostDetails : Route("post_details")
    data object AssetDetails : Route("asset_details")
    data object Search : Route("search")
}
