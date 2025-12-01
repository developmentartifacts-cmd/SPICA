package com.gibson.spica.navigation

object NavGraphs {

    val authGraph = listOf(
        Route.Welcome,
        Route.Signup,
        Route.Login,
        Route.EmailVerification,
        Route.AccountSetup,
    )

    val mainGraph = listOf(
        Route.Home,
        Route.Exchange,
        Route.Portfolio,
        Route.Watchlist,
    )

    val fullScreenGraph = listOf(
        Route.Settings,
        Route.PostDetails,
        Route.AssetDetails,
        Route.Search,
    )
}
