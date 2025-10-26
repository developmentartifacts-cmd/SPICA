package com.gibson.spica.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val step = viewModel.stepIndex
    val scroll = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scroll)
                .padding(20.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                StepHeader(current = step)
                Spacer(modifier = Modifier.height(12.dp))

                when (step) {
                    0 -> StepNames(viewModel)
                    1 -> StepLocation(viewModel)
                    2 -> StepPhoneExtra(viewModel)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    if (step > 0) {
                        OutlinedButton(onClick = { viewModel.prevStep() }) {
                            Text("Back")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Button(onClick = { viewModel.nextStep() }, enabled = !viewModel.isLoading) {
                        Text(
                            when {
                                viewModel.isLoading -> "Saving..."
                                step < 2 -> "Next"
                                else -> "Finish"
                            }
                        )
                    }
                }

                viewModel.message?.let { msg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = msg, color = MaterialTheme.colorScheme.error)
                }
            }

            if (viewModel.showConfirmDialog) {
                ConfirmSaveDialog(
                    onDismiss = { viewModel.showConfirmDialog = false },
                    onConfirm = { viewModel.confirmSave() }
                )
            }
        }
    }
}

@Composable
private fun ConfirmSaveDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Save") },
        text = { Text("Are you sure you want to complete your account setup?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes, Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
