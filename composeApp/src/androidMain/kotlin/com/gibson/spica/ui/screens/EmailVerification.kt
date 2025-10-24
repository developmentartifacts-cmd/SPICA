package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.EmailVerifyViewModel

@Composable
fun EmailVerifyScreen(viewModel: EmailVerifyViewModel = remember { EmailVerifyViewModel() }) {
    val state = viewModel.state

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
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
                Text(
                    "Verify Your Email",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    "A verification link has been sent to your registered email.\nPlease check your inbox and click the link to verify your account.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Button(
                    onClick = { viewModel.checkVerificationStatus() },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isLoading) "Checking..." else "Check Verification Status")
                }

                Button(
                    onClick = { viewModel.resendVerificationEmail() },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isLoading) "Resending..." else "Resend Verification Email")
                }

                TextButton(onClick = { viewModel.skipVerification() }) {
                    Text("Skip (Continue Anyway)")
                }

                if (state.message != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = state.message!!,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}
