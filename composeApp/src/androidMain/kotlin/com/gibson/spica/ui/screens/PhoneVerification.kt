package com.gibson.spica.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.PhoneVerifyViewModel

@Composable
fun PhoneVerifyScreen(
    viewModel: PhoneVerifyViewModel = remember { PhoneVerifyViewModel() }
) {
    val state = viewModel.state
    val activity = LocalContext.current as Activity

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ─────────────────────────────
                // 🔹 Title
                // ─────────────────────────────
                Text(
                    text = "Phone Verification",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                // ─────────────────────────────
                // 🔹 Phone input
                // ─────────────────────────────
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.updatePhoneNumber(it) },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // ─────────────────────────────
                // 🔹 Verification logic
                // ─────────────────────────────
                if (state.verificationId == null) {
                    Button(
                        onClick = { viewModel.sendVerificationCode(activity) },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.isLoading) "Sending..." else "Send Code")
                    }
                } else {
                    OutlinedTextField(
                        value = state.code,
                        onValueChange = { viewModel.updateCode(it) },
                        label = { Text("Verification Code") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { viewModel.verifyCode() },
                        enabled = !state.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.isLoading) "Verifying..." else "Verify Code")
                    }
                }

                // ─────────────────────────────
                // 🔹 Skip option
                // ─────────────────────────────
                TextButton(onClick = { viewModel.skipVerification() }) {
                    Text("Skip for now")
                }

                // ─────────────────────────────
                // 🔹 Message feedback
                // ─────────────────────────────
                if (state.message != null) {
                    Text(
                        text = state.message!!,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
