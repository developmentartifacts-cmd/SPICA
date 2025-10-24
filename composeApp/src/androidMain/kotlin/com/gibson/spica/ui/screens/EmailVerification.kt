package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.data.AuthService
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun EmailVerifyScreen() {
    val auth = remember { FirebaseAuth.getInstance() }
    var isVerified by remember { mutableStateOf(auth.currentUser?.isEmailVerified ?: false) }
    var message by remember { mutableStateOf<String?>(null) }
    var isSending by remember { mutableStateOf(false) }

    // Refresh user state each time screen is opened
    LaunchedEffect(Unit) {
        auth.currentUser?.reload()
        isVerified = auth.currentUser?.isEmailVerified ?: false
    }

    if (isVerified) {
        Router.navigate(Screen.AccountSetup.route)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Verify Your Email",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "We've sent a verification link to your email.\nPlease check your inbox and click the link.",
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val user = auth.currentUser
                    if (user != null && !isSending) {
                        isSending = true
                        user.sendEmailVerification()
                            .addOnCompleteListener { task ->
                                isSending = false
                                message = if (task.isSuccessful) {
                                    "Verification email sent successfully."
                                } else {
                                    "Failed to send verification email: ${task.exception?.message}"
                                }
                            }
                    } else {
                        message = "User not found or already verified."
                    }
                },
                enabled = !isSending,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSending) "Sending..." else "Resend Email")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    auth.currentUser?.reload()?.addOnCompleteListener {
                        isVerified = auth.currentUser?.isEmailVerified ?: false
                        if (isVerified) {
                            Router.navigate(Screen.AccountSetup.route)
                        } else {
                            message = "Email not verified yet. Please check again."
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("I've Verified My Email")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    AuthService.signOut {
                        Router.navigate(Screen.Login.route)
                    }
                }
            ) {
                Text("Back to Login", color = MaterialTheme.colorScheme.error)
            }

            if (message != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = message ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 14.sp
                )
            }
        }
    }
}
