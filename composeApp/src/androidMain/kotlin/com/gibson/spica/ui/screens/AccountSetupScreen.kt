package com.gibson.spica.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.media.AndroidMediaPicker
import com.gibson.spica.viewmodel.AccountSetupViewModel
import android.graphics.BitmapFactory

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val scroll = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scroll)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Account Setup (Step ${viewModel.currentStep} of 3)", fontSize = 20.sp)

            Spacer(Modifier.height(20.dp))

            // 🖼 Profile + Cover Upload
            ProfileAndCoverSection(viewModel)

            Spacer(Modifier.height(20.dp))

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
                ) { Text(if (viewModel.currentStep < 3) "Next" else "Finish") }
            }
        }

        if (viewModel.showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showConfirmationDialog = false },
                title = { Text("Confirm Save") },
                text = { Text("Save account info and proceed?") },
                confirmButton = { TextButton(onClick = { viewModel.saveAccountData() }) { Text("Yes") } },
                dismissButton = { TextButton(onClick = { viewModel.showConfirmationDialog = false }) { Text("Cancel") } }
            )
        }

        if (viewModel.isSaving) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

/* 🧩 Profile + Cover Upload Section */
@Composable
fun ProfileAndCoverSection(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Profile Image
        Text("Profile Picture", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        AndroidMediaPicker(
            mimeFilter = "image/*",
            onFilePicked = { file ->
                viewModel.setProfilePhoto(file)
            }
        )
        viewModel.profilePhoto?.let {
            Spacer(Modifier.height(8.dp))
            Image(
                bitmap = BitmapFactory.decodeByteArray(it.bytes, 0, it.bytes.size).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        // Cover Image
        Text("Cover Photo", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))
        AndroidMediaPicker(
            mimeFilter = "image/*",
            onFilePicked = { file ->
                viewModel.setCoverPhoto(file)
            }
        )
        viewModel.coverPhoto?.let {
            Spacer(Modifier.height(8.dp))
            Image(
                bitmap = BitmapFactory.decodeByteArray(it.bytes, 0, it.bytes.size).asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
    }
}
