@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import coil.compose.AsyncImage
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
                text = "Account Setup (Step ${viewModel.currentStep} of 4)",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            when (viewModel.currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepPhotos(viewModel)
                4 -> StepPhone(viewModel)
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
                        if (viewModel.currentStep < 4) {
                            viewModel.nextStep()
                        } else {
                            viewModel.showConfirmationDialog = true
                        }
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

/* ---------------- STEP 1 ---------------- */
@Composable
fun StepNames(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* ---------------- STEP 2 ---------------- */
@Composable
fun StepBio(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var expandedCountry by remember { mutableStateOf(false) }
        var expandedState by remember { mutableStateOf(false) }
        var expandedTown by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(expanded = expandedCountry, onExpandedChange = { expandedCountry = !expandedCountry }) {
            OutlinedTextField(
                value = viewModel.selectedCountry,
                onValueChange = {},
                label = { Text("Country") },
                readOnly = true,
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedCountry, onDismissRequest = { expandedCountry = false }) {
                viewModel.countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            viewModel.selectedCountry = country
                            viewModel.selectedState = ""
                            viewModel.selectedTown = ""
                            expandedCountry = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (viewModel.selectedCountry.isNotEmpty()) {
            ExposedDropdownMenuBox(expanded = expandedState, onExpandedChange = { expandedState = !expandedState }) {
                OutlinedTextField(
                    value = viewModel.selectedState,
                    onValueChange = {},
                    label = { Text("State") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedState, onDismissRequest = { expandedState = false }) {
                    viewModel.states.forEach { state ->
                        DropdownMenuItem(
                            text = { Text(state) },
                            onClick = {
                                viewModel.selectedState = state
                                viewModel.selectedTown = ""
                                expandedState = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (viewModel.selectedState.isNotEmpty()) {
            ExposedDropdownMenuBox(expanded = expandedTown, onExpandedChange = { expandedTown = !expandedTown }) {
                OutlinedTextField(
                    value = viewModel.selectedTown,
                    onValueChange = {},
                    label = { Text("Town") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedTown, onDismissRequest = { expandedTown = false }) {
                    viewModel.towns.forEach { town ->
                        DropdownMenuItem(
                            text = { Text(town) },
                            onClick = {
                                viewModel.selectedTown = town
                                expandedTown = false
                            }
                        )
                    }
                }
            }
        }
    }
}

/* ---------------- STEP 3 ---------------- */
@Composable
fun StepPhotos(viewModel: AccountSetupViewModel) {
    val profilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) viewModel.profileImageUri = uri
    }

    val coverPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) viewModel.coverImageUri = uri
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Upload Photos", fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(10.dp))

        AsyncImage(
            model = viewModel.coverImageUri ?: viewModel.coverImageUrl,
            contentDescription = "Cover Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { coverPicker.launch("image/*") }) {
            Text("Select Cover Photo")
        }

        Spacer(modifier = Modifier.height(20.dp))

        AsyncImage(
            model = viewModel.profileImageUri ?: viewModel.profileImageUrl,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { profilePicker.launch("image/*") }) {
            Text("Select Profile Photo")
        }
    }
}

/* ---------------- STEP 4 ---------------- */
@Composable
fun StepPhone(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.phoneNumber = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
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
