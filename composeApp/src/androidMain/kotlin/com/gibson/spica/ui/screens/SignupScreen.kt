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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown

@Composable
fun SignupScreen(viewModel: AuthViewModel = AuthViewModel()) {

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

        // ----------------------------------------------------
        // EMAIL SIGNUP (DEFAULT)
        // ----------------------------------------------------
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
                Checkbox(
                    checked = viewModel.termsAccepted,
                    onCheckedChange = { viewModel.termsAccepted = it }
                )
                Spacer(Modifier.width(8.dp))
                Text("I agree to SPICA's Terms & Privacy")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { viewModel.signupEmail() },
                enabled =
                viewModel.termsAccepted &&
                        viewModel.email.isNotBlank() &&
                        viewModel.password.isNotBlank() &&
                        viewModel.confirmPassword.isNotBlank() &&
                        !viewModel.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (viewModel.isLoading) "Creating..." else "Sign up")
            }
        }

        // ----------------------------------------------------
        // PHONE SIGNUP (OTP)
        // ----------------------------------------------------
        if (mode == "phone") {

            var ccExpanded by remember { mutableStateOf(false) }

            OutlinedTextField(
                value = "${viewModel.dialCode} ${viewModel.phoneNumber}",
                onValueChange = {},
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { ccExpanded = true }
                    )
                }
            )

            DropdownMenu(
                expanded = ccExpanded,
                onDismissRequest = { ccExpanded = false }
            ) {
                CountryCodeData.list.forEach { cc ->
                    DropdownMenuItem(
                        text = { Text("${cc.name} ${cc.dialCode}") },
                        onClick = {
                            viewModel.dialCode = cc.dialCode
                            ccExpanded = false
                        }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            if (!viewModel.otpSent) {

                OutlinedTextField(
                    value = viewModel.phoneNumber,
                    onValueChange = { viewModel.phoneNumber = it },
                    label = { Text("Phone number only") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = viewModel.termsAccepted,
                        onCheckedChange = { viewModel.termsAccepted = it }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("I agree to SPICA's Terms & Privacy")
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = {
                        val full = "${viewModel.dialCode}${viewModel.phoneNumber}".replace(" ", "")
                        viewModel.startPhoneVerification(full)
                    },
                    enabled =
                    viewModel.phoneNumber.isNotBlank() &&
                            viewModel.termsAccepted &&
                            !viewModel.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Request code")
                }

            } else {

                Spacer(Modifier.height(8.dp))
                Text("Enter code sent to ${viewModel.dialCode} ${viewModel.phoneNumber}")

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Button(
                        onClick = { viewModel.verifyPhoneOtp() },
                        enabled = viewModel.otpCode.length >= 4 && !viewModel.isLoading,
                        modifier = Modifier.weight(1f)
                    ) { Text("Verify") }

                    OutlinedButton(
                        onClick = {
                            val full = "${viewModel.dialCode}${viewModel.phoneNumber}".replace(" ", "")
                            viewModel.resendOtp(full)
                        },
                        enabled = viewModel.otpCountdown == 0,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (viewModel.otpCountdown > 0)
                            Text("Resend in ${viewModel.otpCountdown}s")
                        else Text("Resend")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ----------------------------------------------------
        // "or" divider
        // ----------------------------------------------------
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f))
            Text("  or  ")
            Divider(modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))

        // ----------------------------------------------------
        // Bottom buttons (Google + Switch mode)
        // ----------------------------------------------------
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Button(
                onClick = {
                    // Android-specific real Google sign-in will be added later
                },
                modifier = Modifier.weight(1f)
            ) { Text("Google") }

            Button(
                onClick = { mode = if (mode == "email") "phone" else "email" },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (mode == "email") "Use Phone" else "Use Email")
            }
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = { Router.navigate(Screen.Login.route) }) {
            Text("Already have an account? Log in")
        }

        // ----------------------------------------------------
        // Error / Success messages
        // ----------------------------------------------------
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
