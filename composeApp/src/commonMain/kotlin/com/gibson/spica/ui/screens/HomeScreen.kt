package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.data.AuthService
import com.gibson.spica.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val currentUser = remember { AuthService.getCurrentUser() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // 🌕 Title
            Text(
                text = "Welcome to SPICA!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary // green accent
            )

            Spacer(Modifier.height(16.dp))

            // 👤 User email
            Text(
                text = "Logged in as: ${currentUser?.email ?: "Guest"}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground // white text
            )

            Spacer(Modifier.height(32.dp))

            // 🚪 Sign out button
            Button(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}
