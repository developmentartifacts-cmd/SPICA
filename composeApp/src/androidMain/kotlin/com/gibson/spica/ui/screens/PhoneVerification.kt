package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.PhoneVerifyViewModel

@Composable
fun PhoneVerifyScreen(viewModel: PhoneVerifyViewModel = remember { PhoneVerifyViewModel() }) {
    val state = viewModel.state
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Phone Verification",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { viewModel.updatePhoneNumber(it) },
                    label = { Text("Phone Number (+234...)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                if (!state.codeSent) {
                    Button(
                        onClick = { viewModel.sendVerificationCode() },
                        enabled = !state.isLoading && state.phoneNumber.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.isLoading) "Sending..." else "Send Verification Code")
                    }
                } else {
                    OutlinedTextField(
                        value = state.verificationCode,
                        onValueChange = { viewModel.updateVerificationCode(it) },
                        label = { Text("Enter Verification Code") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Button(
                        onClick = { viewModel.verifyCode() },
                        enabled = !state.isLoading && state.verificationCode.isNotBlank(),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.isLoading) "Verifying..." else "Verify Code")
                    }

                    TextButton(onClick = { viewModel.resendCode() }) {
                        Text("Resend Code")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { viewModel.skipVerification() }) {
                    Text("Skip Phone Verification")
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
