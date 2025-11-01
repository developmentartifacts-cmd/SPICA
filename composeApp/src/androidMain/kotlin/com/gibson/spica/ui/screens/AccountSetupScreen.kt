@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.ui.MediaPickerViewSimple

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scroll),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Account Setup (Step ${viewModel.currentStep} of 3)", style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(16.dp))

            // 🔹 Profile & Cover Uploads
            Text("Upload Profile & Cover Photos", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            MediaPickerViewSimple(
                label = "Upload Profile Photo",
                isUploading = viewModel.profileUploading,
                onFileSelected = { bytes -> viewModel.uploadImage(bytes, isProfile = true) },
                uploaded = viewModel.profilePhotoUrl != null
            )

            Spacer(Modifier.height(12.dp))

            MediaPickerViewSimple(
                label = "Upload Cover Photo",
                isUploading = viewModel.coverUploading,
                onFileSelected = { bytes -> viewModel.uploadImage(bytes, isProfile = false) },
                uploaded = viewModel.coverPhotoUrl != null
            )

            Spacer(Modifier.height(24.dp))

            // 🔹 Steps
            when (viewModel.currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepPhone(viewModel)
            }

            Spacer(Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.currentStep > 1) {
                    Button(onClick = { viewModel.previousStep() }) { Text("Back") }
                }

                Button(
                    onClick = {
                        if (viewModel.currentStep < 3) viewModel.nextStep()
                        else viewModel.showConfirmationDialog = true
                    }
                ) {
                    Text(if (viewModel.currentStep < 3) "Next" else "Finish")
                }
            }
        }

        // 🔹 Confirmation Dialog
        if (viewModel.showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showConfirmationDialog = false },
                title = { Text("Confirm Save") },
                text = { Text("Save your account details and continue?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.saveAccountData() }) { Text("Yes") }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showConfirmationDialog = false }) { Text("Cancel") }
                }
            )
        }

        if (viewModel.isSaving) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
