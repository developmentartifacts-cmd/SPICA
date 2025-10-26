package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AccountSetupSuccessScreen() {
    val firestore = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val userId = auth.currentUser?.uid

    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 🔁 Load Firestore user document when screen opens
    LaunchedEffect(Unit) {
        if (userId == null) {
            errorMessage = "User not logged in"
            isLoading = false
            return@LaunchedEffect
        }

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    userData = snapshot.data
                } else {
                    errorMessage = "No user document found"
                }
                isLoading = false
            }
            .addOnFailureListener { e ->
                errorMessage = e.localizedMessage
                isLoading = false
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }

            errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "⚠️ ${errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { Router.navigate(Screen.AccountSetup.route) }) {
                        Text("Retry Setup")
                    }
                }
            }

            userData != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "✅ Account Setup Complete!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Welcome, ${userData?.get("firstName")} ${userData?.get("lastName")}",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Username: ${userData?.get("username") ?: "N/A"}")
                    Text("Email: ${auth.currentUser?.email ?: "N/A"}")
                    Text("Country: ${userData?.get("country") ?: "N/A"}")
                    Text("State: ${userData?.get("state") ?: "N/A"}")
                    Text("City: ${userData?.get("town") ?: "N/A"}")
                    Text("Postcode: ${userData?.get("postcode") ?: "N/A"}")
                    Text("Phone: ${userData?.get("phone") ?: "N/A"}")

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { Router.navigate(Screen.Home.route) },
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Text("Continue to Home")
                    }
                }
            }
        }
    }
}
