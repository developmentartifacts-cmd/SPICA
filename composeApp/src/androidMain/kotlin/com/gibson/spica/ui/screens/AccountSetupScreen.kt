@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.platform.readPlatformFileFromUri
import com.gibson.spica.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val scrollState = rememberScrollState()

    // Launchers for picking image/file (profile & cover)
    val context = LocalContext.current

    val profilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val pf = readPlatformFileFromUri(context, uri)
            pf?.let { viewModel.setProfileFile(it) }
        }
    }

    val coverPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val pf = readPlatformFileFromUri(context, uri)
            pf?.let { viewModel.setCoverFile(it) }
        }
    }

    // Previews: decode bytes if available
    val profileBitmap by remember(viewModel.profileFile) {
        mutableStateOf(viewModel.profileFile?.let {
            BitmapFactory.decodeByteArray(it.bytes, 0, it.bytes.size)
        })
    }

    val coverBitmap by remember(viewModel.coverFile) {
        mutableStateOf(viewModel.coverFile?.let {
            BitmapFactory.decodeByteArray(it.bytes, 0, it.bytes.size)
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "Account Setup (Step ${viewModel.currentStep} of 4)",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (viewModel.currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> {
                    // Photos step with platform picker
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Upload Cover Photo")
                        Spacer(modifier = Modifier.height(8.dp))
                        if (coverBitmap != null) {
                            Image(
                                bitmap = coverBitmap!!.asImageBitmap(),
                                contentDescription = "Cover preview",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No cover selected")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { coverPicker.launch("image/*") }) {
                            Text("Select Cover Photo")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Upload Profile Photo")
                        Spacer(modifier = Modifier.height(8.dp))

                        if (profileBitmap != null) {
                            Image(
                                bitmap = profileBitmap!!.asImageBitmap(),
                                contentDescription = "Profile preview",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No profile")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { profilePicker.launch("image/*") }) {
                            Text("Select Profile Photo")
                        }
                    }
                }
                4 -> StepPhone(viewModel)
            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.currentStep > 1) {
                    Button(onClick = { viewModel.previousStep() }) { Text("Back") }
                }

                Button(
                    onClick = {
                        if (viewModel.currentStep < 4) viewModel.nextStep()
                        else viewModel.showConfirmationDialog = true
                    }
                ) {
                    Text(if (viewModel.currentStep < 4) "Next" else "Finish")
                }
            }
        }

        if (viewModel.showConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showConfirmationDialog = false },
                title = { Text("Confirm Save") },
                text = { Text("Do you want to save your account information and proceed?") },
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
