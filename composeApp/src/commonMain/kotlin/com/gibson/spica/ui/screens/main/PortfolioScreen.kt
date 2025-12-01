package com.gibson.spica.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.navigation.Navigator
import com.gibson.spica.navigation.Route

@Composable
fun PortfolioScreen(navigator: Navigator) {
    MainTabContainer(title = "Portfolio", navigator = navigator) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Your Portfolio", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Holdings, P&L, charts, and positions.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { navigator.navigate(Route.Search) }) {
                    Text("Search Asset")
                }
            }
        }
    }
}
