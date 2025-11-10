package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AuthViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun SignupScreen(viewModel: AuthViewModel = AuthViewModel()) {
    val scaffoldPadding = 24.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = scaffoldPadding)
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(50),
            tonalElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = { viewModel.confirmPassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = viewModel.termsAccepted, onCheckedChange = { viewModel.termsAccepted = it })
            Spacer(Modifier.width(8.dp))
            Text("I have read and agree to SPICA's Terms of Service and Privacy Policy")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.signupEmail() },
            enabled = viewModel.termsAccepted && viewModel.email.isNotBlank() && viewModel.password.isNotBlank() && viewModel.confirmPassword.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(if (viewModel.isLoading) "Creating..." else "Sign Up")
        }

        Spacer(Modifier.height(12.dp))
        // pill divider style (visual separator)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f).height(1.dp))
            Text("  or sign up with  ", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.weight(1f).height(1.dp))
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            // Google button (pill)
            OutlinedButton(
                onClick = {
                    // Platform-specific: launch Google sign-in flow and then call viewModel.signupWithGoogle(idToken)
                    // TODO: implement Android GoogleSignIn integration in Android module and pass idToken to viewModel
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50)
            ) {
                Text("Google")
            }

            // Phone button toggles phone mode — here we'll navigate to show phone onboarding or show inline fields
            OutlinedButton(
                onClick = {
                    // Could open a phone signup dialog or switch UI to phone mode
                    // For now: navigate to Login for phone flow (or open a bottom-sheet)
                    Router.navigate(Screen.Login.route)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(50)
            ) {
                Text("Phone")
            }
        }

        Spacer(Modifier.height(24.dp))

        TextButton(onClick = { /* admin placeholder */ }) {
            Text("Signup as administrator")
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
