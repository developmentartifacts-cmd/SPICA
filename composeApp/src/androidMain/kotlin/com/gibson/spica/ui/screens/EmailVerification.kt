package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.EmailVerifyViewModel

@Composable
fun EmailVerifyScreen(viewModel: EmailVerifyViewModel = EmailVerifyViewModel()) {
    val state = viewModel.state

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 24.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Verify Your Email", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Text("A verification link has been sent to your registered email. Please check your inbox and click the link to verify your account.", style = MaterialTheme.typography.bodyMedium)
                Button(onClick = { viewModel.checkVerificationStatus() }, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
                    Text(if (state.isLoading) "Checking..." else "Check Verification Status")
                }
                Button(onClick = { viewModel.resendVerificationEmail() }, enabled = !state.isLoading, modifier = Modifier.fillMaxWidth()) {
                    Text(if (state.isLoading) "Resending..." else "Resend Verification Email")
                }
                TextButton(onClick = { viewModel.skipVerification() }) { Text("Skip (Continue Anyway)") }
                state.message?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}
