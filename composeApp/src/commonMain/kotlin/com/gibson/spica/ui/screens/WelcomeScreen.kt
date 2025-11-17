package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Removed import com.gibson.spica.navigation.Router
// Removed import com.gibson.spica.navigation.Screen

@Composable
fun WelcomeScreen(
    // 💡 New: Accepts navigation actions as lambda parameters (State Hoisting)
    onNavigateToSignup: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Welcome to SPICA",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Your world of limitless connection, creation, and discovery.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(40.dp))

            Button(
                onClick = onNavigateToSignup, // 💡 Calls the injected lambda
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Started")
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNavigateToLogin, // 💡 Calls the injected lambda
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Already have an account? Log In")
            }
        }
    }
}
