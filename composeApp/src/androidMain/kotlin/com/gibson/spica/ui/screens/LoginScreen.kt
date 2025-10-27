package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    message = null
                    loading = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = auth.currentUser
                                if (user == null) {
                                    message = "Authentication failed. Please try again."
                                    loading = false
                                    return@addOnCompleteListener
                                }

                                if (!user.isEmailVerified) {
                                    loading = false
                                    Router.navigate(Screen.EmailVerify.route)
                                    return@addOnCompleteListener
                                }

                                // 🔹 Check Firestore for user document
                                firestore.collection("users").document(user.uid)
                                    .get()
                                    .addOnSuccessListener { doc ->
                                        loading = false
                                        if (doc.exists()) {
                                            Router.navigate(Screen.Home.route)
                                        } else {
                                            Router.navigate(Screen.AccountSetup.route)
                                        }
                                    }
                                    .addOnFailureListener {
                                        loading = false
                                        message = "Error loading user data. Try again."
                                    }
                            } else {
                                loading = false
                                message = task.exception?.localizedMessage
                                    ?: "Login failed. Please check your credentials."
                            }
                        }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Logging in...")
                } else {
                    Text("Login")
                }
            }

            Spacer(Modifier.height(12.dp))
            TextButton(onClick = { Router.navigate(Screen.Signup.route) }) {
                Text("Don't have an account? Sign Up")
            }

            message?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
