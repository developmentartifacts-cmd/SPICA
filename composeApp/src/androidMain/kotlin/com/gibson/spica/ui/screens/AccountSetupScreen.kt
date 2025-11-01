@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.viewmodel.MediaPickerViewModel
import com.gibson.spica.ui.MediaPickerView

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val scrollState = rememberScrollState()

    // 🔹 Media pickers for profile + cover
    val profileMediaVM = remember { MediaPickerViewModel() }
    val coverMediaVM = remember { MediaPickerViewModel() }

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

            Spacer(modifier = Modifier.height(16.dp))

            // 🖼 Profile + Cover Upload
            Text("Profile Picture", fontWeight = FontWeight.SemiBold)
            MediaPickerView(viewModel = profileMediaVM)
            Spacer(modifier = Modifier.height(12.dp))

            Text("Cover Photo", fontWeight = FontWeight.SemiBold)
            MediaPickerView(viewModel = coverMediaVM)

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Step forms
            when (viewModel.currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepPhone(viewModel)
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
                            // Save URLs before finishing
                            viewModel.profilePhotoUrl = profileMediaVM.downloadUrl.value
                            viewModel.coverPhotoUrl = coverMediaVM.downloadUrl.value
                            viewModel.showConfirmationDialog = true
                        }
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
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(10.dp))
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

        Spacer(Modifier.height(10.dp))

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

        Spacer(Modifier.height(10.dp))

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
fun StepPhone(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
