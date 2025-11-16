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
fun LoginScreen(viewModel: AuthViewModel = AuthViewModel()) {
    var mode by remember { mutableStateOf("email") } // "email" or "phone"
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

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

            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { viewModel.loginEmail() },
                enabled = viewModel.email.isNotBlank() && viewModel.password.isNotBlank() && !viewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoading) "Logging in..." else "Login")
            }
        }

        if (mode == "phone") {
            var expanded by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = "${viewModel.dialCode} ${viewModel.phoneNumber}",
                onValueChange = {},
                label = { Text("Phone (tap to pick country code)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                singleLine = true,
                readOnly = true
            )
            Spacer(Modifier.height(8.dp))
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                CountryCodeData.list.forEach { cc ->
                    DropdownMenuItem(text = { Text("${cc.name} ${cc.dialCode}") }, onClick = {
                        viewModel.dialCode = cc.dialCode
                        expanded = false
                    })
                }
            }

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
                        onClick = { viewModel.verifyPhoneOtp() },
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

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f).height(1.dp))
            Text("  or  ", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.weight(1f).height(1.dp))
        }

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = {
                    // TODO: launch Google platform sign-in, then call viewModel.signupWithGoogle(idToken)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Sign in with Google")
            }
            Button(onClick = { mode = if (mode == "email") "phone" else "email" }, modifier = Modifier.weight(1f)) {
                Text(if (mode == "email") "Use Phone" else "Use Email")
            }
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = { Router.navigate(Screen.Signup.route) }) {
            Text("Don't have an account? Sign up")
        }

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
