package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun AccountSetupSuccessScreen() {
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val userId = auth.currentUser?.uid

    var userData by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Live load from Firestore
    LaunchedEffect(userId) {
        if (userId == null) {
            errorMessage = "User not logged in"
            isLoading = false
            return@LaunchedEffect
        }

        firestore.collection("users").document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    errorMessage = e.localizedMessage
                    isLoading = false
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    userData = snapshot.data
                    isLoading = false
                } else {
                    errorMessage = "No user record found."
                    isLoading = false
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> CircularProgressIndicator()

            errorMessage != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⚠️ ${errorMessage}", color = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { Router.navigate(Screen.AccountSetup.route) }) {
                        Text("Retry Setup")
                    }
                }
            }

            userData != null -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✅ Account Setup Complete!", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(16.dp))

                    Text("Welcome, ${userData?.get("firstName")} ${userData?.get("lastName")}", fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))

                    Divider(Modifier.padding(vertical = 8.dp))

                    listOf(
                        "Username" to userData?.get("username"),
                        "Email" to auth.currentUser?.email,
                        "Phone" to userData?.get("phone"),
                        "Country" to userData?.get("country"),
                        "State" to userData?.get("state"),
                        "Town" to userData?.get("town"),
                        "Bio" to userData?.get("bio")
                    ).forEach { (label, value) ->
                        if (value != null && value.toString().isNotBlank()) {
                            Text("$label: $value", fontSize = 16.sp)
                            Spacer(Modifier.height(4.dp))
                        }
                    }

                    Spacer(Modifier.height(24.dp))
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
