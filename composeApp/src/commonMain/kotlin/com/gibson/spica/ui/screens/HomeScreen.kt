package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.gibson.spica.viewmodel.AuthViewModel

/**
 * Main authenticated screen for SPICA.
 * Displays basic user info and sign-out action.
 */
@Composable
fun HomeScreen(viewModel: AuthViewModel = viewModel()) {
    val user = viewModel.user

    // ✅ If not logged in, redirect to Login
    if (user == null) {
        Router.navigate(Screen.Login.route)
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to SPICA 🌾",
                fontSize = 26.sp,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(Modifier.height(8.dp))
            Text(
                text = "Logged in as: ${user.email ?: "Anonymous"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { viewModel.signOut() },
                modifier = Modifier.height(48.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}
