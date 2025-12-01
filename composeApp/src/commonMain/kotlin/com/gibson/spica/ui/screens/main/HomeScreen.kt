package com.gibson.spica.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.navigation.Navigator
import com.gibson.spica.navigation.Route

@Composable
fun HomeScreen(navigator: Navigator) {
    MainTabContainer(title = "Home", navigator = navigator) {

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text("Welcome to SPICA", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Home Screen — dashboard widgets go here.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { navigator.navigate(Route.AssetDetails) }) {
                    Text("Open an Asset")
                }
            }
        }
    }
}
