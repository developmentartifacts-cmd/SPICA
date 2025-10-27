package com.gibson.spica.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun PhoneVerifyScreen() {
    val auth = FirebaseAuth.getInstance()

    var phoneNumber by remember { mutableStateOf(auth.currentUser?.phoneNumber ?: "") }
    var verificationCode by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val activity = LocalContext.current as? Activity

    LaunchedEffect(Unit) {
        // Auto-redirect if phone already verified
        if (auth.currentUser?.phoneNumber != null) {
            Router.navigate(Screen.AccountSetupSuccess.route)
        }
    }

    fun sendVerification() {
        if (phoneNumber.isEmpty() || activity == null) {
            message = "Please enter a valid phone number."
            return
        }

        isLoading = true

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            message = "Phone verified successfully!"
                            Router.navigate(Screen.AccountSetupSuccess.route)
                        } else {
                            message = "Verification failed: ${task.exception?.localizedMessage}"
                        }
                    }
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    isLoading = false
                    message = "Error: ${e.localizedMessage}"
                }

                override fun onCodeSent(vid: String, token: PhoneAuthProvider.ForceResendingToken) {
                    isLoading = false
                    verificationId = vid
                    message = "Verification code sent!"
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyCode() {
        val vid = verificationId
        if (vid == null || verificationCode.isEmpty()) {
            message = "Enter a valid code."
            return
        }

        isLoading = true
        val credential = PhoneAuthProvider.getCredential(vid, verificationCode)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            isLoading = false
            if (task.isSuccessful) {
                message = "Verification successful!"
                Router.navigate(Screen.AccountSetupSuccess.route)
            } else {
                message = "Invalid code or error occurred."
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("📱 Phone Verification", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(onClick = { sendVerification() }, modifier = Modifier.fillMaxWidth()) {
                Text("Send Verification Code")
            }

            Spacer(Modifier.height(24.dp))

            if (verificationId != null) {
                OutlinedTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    label = { Text("Enter Code") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Button(onClick = { verifyCode() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Verify Code")
                }
            }

            Spacer(Modifier.height(16.dp))

            message?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
            }

            if (isLoading) {
                Spacer(Modifier.height(20.dp))
                CircularProgressIndicator()
            }
        }
    }
}
