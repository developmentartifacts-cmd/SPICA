@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gibson.spica.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Account Setup (Step ${viewModel.currentStep} of 3)",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (viewModel.currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepPhoto(viewModel) // 🔹 NEW — profile & cover step
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.currentStep > 1) {
                    Button(onClick = { viewModel.previousStep() }) {
                        Text("Back")
                    }
                }

                Button(
                    onClick = {
                        if (viewModel.currentStep < 3) {
                            viewModel.nextStep()
                        } else {
                            viewModel.showConfirmationDialog = true
                        }
                    }
                ) {
                    Text(if (viewModel.currentStep < 3) "Next" else "Finish")
                }
            }
        }

        if (viewModel.showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showConfirmationDialog = false },
                title = { Text("Confirm Save") },
                text = { Text("Save your account info and upload photos?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.saveAccountData() }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showConfirmationDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (viewModel.isSaving) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

/* ---------------- STEP 3 ---------------- */
@Composable
fun StepPhoto(viewModel: AccountSetupViewModel) {
    // 🔹 NEW: Selectors
    val profileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.onProfileSelected(it) } }

    val coverLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.onCoverSelected(it) } }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Profile Picture", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { profileLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.profileUri != null)
                Image(
                    painter = rememberAsyncImagePainter(viewModel.profileUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            else
                Text("Tap to upload")
        }

        Spacer(Modifier.height(25.dp))

        Text("Cover Photo", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable { coverLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (viewModel.coverUri != null)
                Image(
                    painter = rememberAsyncImagePainter(viewModel.coverUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            else
                Text("Tap to upload cover photo")
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.bio,
            onValueChange = { viewModel.bio = it },
            label = { Text("Short Bio / Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}
