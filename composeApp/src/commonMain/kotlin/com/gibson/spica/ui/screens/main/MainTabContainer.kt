package com.gibson.spica.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.navigation.Navigator
import com.gibson.spica.navigation.Route
import com.gibson.spica.ui.AppNavBar

@Composable
fun MainTabContainer(
    title: String,
    navigator: Navigator,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = { Text(title, style = MaterialTheme.typography.titleLarge) },
            actions = {
                IconButton(onClick = { navigator.navigate(Route.Settings) }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            content = content
        )

        AppNavBar(
            currentRoute = navigator.currentRoute().path,
            onItemClick = { route ->
                when (route) {
                    Route.Home.path -> navigator.navigate(Route.Home)
                    Route.Exchange.path -> navigator.navigate(Route.Exchange)
                    Route.Portfolio.path -> navigator.navigate(Route.Portfolio)
                    Route.Watchlist.path -> navigator.navigate(Route.Watchlist)
                }
            }
        )
    }
}
