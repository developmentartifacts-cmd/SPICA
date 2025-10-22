//NavigationModel.kt
package com.gibson.spica.ui

import androidx.compose.runtime.Immutable
import com.gibson.spica.navigation.Screen

@Immutable
data class NavItem(val route: String, val label: String)

/**
 * Bottom navigation items (mobile: bottom pill bar).
 * Order: Home | Marketplace | Portfolio | Watchlist
 */
val MainNavItems = listOf(
    NavItem(route = Screen.Home.route, label = "Home"),
    NavItem(route = Screen.Marketplace.route, label = "Marketplace"),
    NavItem(route = Screen.Portfolio.route, label = "Portfolio"),
    NavItem(route = Screen.Watchlist.route, label = "Watchlist")
)
