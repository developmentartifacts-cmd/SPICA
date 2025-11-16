package com.gibson.spica.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AuthViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import com.gibson.spica.data.CountryCodeData

@Composable
fun SignupScreen(viewModel: AuthViewModel = AuthViewModel()) {
    // UI-mode: "email" | "phone" | "google" (google handled via button)
    var mode by remember { mutableStateOf("email") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        // -------------------
        // Email mode (default)
        // -------------------
        if (mode == "email") {
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                label = { Text("Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.confirmPassword,
                onValueChange = { viewModel.confirmPassword = it },
                label = { Text("Confirm Password") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // stable independent checkbox state in viewModel.termsAccepted
                Checkbox(
                    checked = viewModel.termsAccepted,
                    onCheckedChange = { viewModel.termsAccepted = it }
                )
                Spacer(Modifier.width(8.dp))
                Text("I have read and agree to SPICA's Terms & Privacy")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { viewModel.signupEmail() },
                enabled = viewModel.termsAccepted &&
                        viewModel.email.isNotBlank() &&
                        viewModel.password.isNotBlank() &&
                        viewModel.confirmPassword.isNotBlank() &&
                        !viewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoading) "Creating..." else "Sign up")
            }
        }

        // -------------------
        // Phone mode (inline OTP)
        // -------------------
        if (mode == "phone") {
            // Country code selector simple dropdown (reuse CountryCodeData)
            var expanded by remember { mutableStateOf(false) }
            val selectedDial = viewModel.dialCode

            OutlinedTextField(
                value = "${viewModel.dialCode} ${viewModel.phoneNumber}",
                onValueChange = {
                    // keep dial and number separate — allow typing the full field for convenience
                    // but primarily update phoneNumber by stripping dial if present
                    val v = it.trim()
                    if (v.startsWith("+")) {
                        // optional: parse dial from start
                        val parts = v.split(" ")
                        if (parts.size > 1) {
                            viewModel.dialCode = parts[0]
                            viewModel.phoneNumber = parts.drop(1).joinToString(" ")
                        } else {
                            viewModel.phoneNumber = ""
                        }
                    } else {
                        viewModel.phoneNumber = v
                    }
                },
                label = { Text("Phone (tap to pick country code)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                singleLine = true,
                readOnly = true
            )

            Spacer(Modifier.height(8.dp))

            // Country code menu (modal-like small dropdown)
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                CountryCodeData.list.forEach { cc ->
                    DropdownMenuItem(text = { Text("${cc.name} ${cc.dialCode}") }, onClick = {
                        viewModel.dialCode = cc.dialCode
                        expanded = false
                    })
                }
            }

            // If OTP not sent yet, show button to request code
            if (!viewModel.otpSent) {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.phoneNumber,
                    onValueChange = { viewModel.phoneNumber = it },
                    label = { Text("Phone number (no country code)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val full = "${viewModel.dialCode}${viewModel.phoneNumber}".replace(" ", "")
                        viewModel.startPhoneVerification(full)
                    },
                    enabled = viewModel.phoneNumber.isNotBlank() && !viewModel.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Request code")
                }
            } else {
                // OTP entry UI (inline)
                Spacer(Modifier.height(8.dp))
                Text("Enter the code sent to ${viewModel.dialCode} ${viewModel.phoneNumber}")

                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.otpCode,
                    onValueChange = { viewModel.otpCode = it },
                    label = { Text("Verification code") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            viewModel.verifyPhoneOtp()
                        },
                        enabled = viewModel.otpCode.length >= 4 && !viewModel.isLoading,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Verify")
                    }

                    OutlinedButton(
                        onClick = {
                            val full = "${viewModel.dialCode}${viewModel.phoneNumber}".replace(" ", "")
                            viewModel.resendOtp(full)
                        },
                        enabled = viewModel.otpCountdown == 0
                    ) {
                        if (viewModel.otpCountdown > 0) Text("Resend in ${viewModel.otpCountdown}s") else Text("Resend")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Divider
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f).height(1.dp))
            Text("  or  ", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.weight(1f).height(1.dp))
        }

        Spacer(Modifier.height(12.dp))

        // Bottom buttons: Google and toggle other method (email/phone)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    // Platform-specific Google sign-in: you must call Android GoogleSignIn to get idToken
                    // Then call viewModel.signupWithGoogle(idToken)
                    // Placeholder here:
                    // TODO: launch Google Sign-in in Android module and call viewModel.signupWithGoogle(token)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sign up with Google")
            }

            Button(
                onClick = {
                    mode = if (mode == "email") "phone" else "email"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (mode == "email") "Use Phone" else "Use Email")
            }
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = { Router.navigate(Screen.Login.route) }) {
            Text("Already have an account? Log in")
        }

        // messages
        viewModel.errorMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
        viewModel.successMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
