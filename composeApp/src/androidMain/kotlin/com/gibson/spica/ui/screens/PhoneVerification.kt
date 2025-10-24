package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.viewmodel.PhoneVerifyViewModel

@Composable
fun PhoneVerifyScreen(viewModel: PhoneVerifyViewModel = viewModel()) {
    val isSending = viewModel.isSending
    val isVerifying = viewModel.isVerifying
    val error = viewModel.errorMessage
    val success = viewModel.successMessage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Phone Verification",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = viewModel.phoneNumber,
                onValueChange = { viewModel.phoneNumber = it },
                label = { Text("Phone Number") },
                enabled = !isSending && !isVerifying,
                singleLine = true,
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.sendCode() },
                enabled = !isSending,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isSending) "Sending..." else "Send Code")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = viewModel.code,
                onValueChange = { viewModel.code = it },
                label = { Text("Verification Code") },
                singleLine = true,
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { viewModel.verifyCode() },
                enabled = !isVerifying,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isVerifying) "Verifying..." else "Verify")
            }

            Spacer(Modifier.height(16.dp))

            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            if (success != null) {
                Text(success, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }

            TextButton(onClick = { viewModel.skip() }) {
                Text("Skip verification → Continue to Home")
            }
        }
    }
}
