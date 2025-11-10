package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel = AuthViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp).padding(top = 32.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text("Login", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        Surface(shape = RoundedCornerShape(50), tonalElevation = 0.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                OutlinedTextField(value = viewModel.email, onValueChange = { viewModel.email = it }, label = { Text("Email") }, singleLine = true, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(value = viewModel.password, onValueChange = { viewModel.password = it }, label = { Text("Password") }, singleLine = true, modifier = Modifier.fillMaxWidth())
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { viewModel.loginEmail() },
            enabled = viewModel.email.isNotBlank() && viewModel.password.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50)
        ) {
            Text(if (viewModel.isLoading) "Logging in..." else "Login")
        }

        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Divider(modifier = Modifier.weight(1f).height(1.dp))
            Text("  or login with  ", style = MaterialTheme.typography.bodyMedium)
            Divider(modifier = Modifier.weight(1f).height(1.dp))
        }
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(onClick = {
                // TODO: launch Google platform flow & call viewModel.signupWithGoogle(idToken)
            }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(50)) {
                Text("Google")
            }

            OutlinedButton(onClick = {
                // TODO: open phone login flow (platform-specific)
            }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(50)) {
                Text("Phone")
            }
        }

        Spacer(Modifier.height(24.dp))
        TextButton(onClick = { /* admin placeholder */ }) { Text("Login as administrator") }

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
