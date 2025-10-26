package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun AccountSetupSuccessScreen(viewModel: AccountSetupViewModel) {
    val snapshot = viewModel.savedSnapshot

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (snapshot == null) {
                Text("No saved data found.")
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("✅ Account Created Successfully!", style = MaterialTheme.typography.titleLarge)
                    Divider()
                    Text("Name: ${snapshot.getString("firstName")} ${snapshot.getString("lastName")}")
                    Text("Username: ${snapshot.getString("username")}")
                    Text("Country: ${snapshot.getString("country")}")
                    Text("State: ${snapshot.getString("state")}")
                    Text("Town: ${snapshot.getString("town")}")

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { Router.navigate(Screen.Home.route) }) {
                        Text("Go to Home")
                    }
                }
            }
        }
    }
}
