package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun EmailVerifyScreen() {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var message by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text("Email Verification", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))
            Text("A verification link has been sent to your email. Please verify your email before continuing.")
            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                user?.reload()?.addOnSuccessListener {
                    if (user.isEmailVerified) {
                        Router.navigate(Screen.AccountSetup.route)
                    } else {
                        message = "Email not verified yet."
                    }
                }
            }) { Text("I've Verified My Email") }

            Spacer(Modifier.height(8.dp))
            message?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }
    }
}
