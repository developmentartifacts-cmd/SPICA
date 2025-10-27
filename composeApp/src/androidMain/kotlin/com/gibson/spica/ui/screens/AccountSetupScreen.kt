package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import network.chaintech.cmp_country_code_picker.picker.CountryPickerBasicTextField
import network.chaintech.cmp_country_code_picker.model.CountryDetails

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel = remember { AccountSetupViewModel() }) {
    val currentStep = viewModel.currentStep
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepHeader(
                currentStep = currentStep,
                totalSteps = 3,
                title = when (currentStep) {
                    1 -> "Your Name"
                    2 -> "Bio Information"
                    3 -> "Phone & Extras"
                    else -> ""
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepPhoneExtra(viewModel)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 1) {
                    TextButton(onClick = { viewModel.prevStep() }) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(64.dp))
                }

                Button(
                    onClick = { viewModel.nextStep() },
                    enabled = !viewModel.isLoading,
                ) {
                    Text(if (currentStep < 3) "Next" else "Finish")
                }
            }

            if (viewModel.showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { viewModel.showConfirmDialog = false },
                    title = { Text("Confirm Setup") },
                    text = { Text("Do you want to complete account setup?") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.submitAccountSetup() }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.showConfirmDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (viewModel.message != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(viewModel.message!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun StepHeader(currentStep: Int, totalSteps: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = currentStep / totalSteps.toFloat(),
            modifier = Modifier.fillMaxWidth(0.8f)
        )
    }
}

@Composable
fun StepNames(viewModel: AccountSetupViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = { Text("First Name *") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.secondName,
            onValueChange = { viewModel.secondName = it },
            label = { Text("Second Name (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Last Name *") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepBio(viewModel: AccountSetupViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text("Username *") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = viewModel.bio,
            onValueChange = { viewModel.bio = it },
            label = { Text("Bio / About you") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )
    }
}

@Composable
fun StepPhoneExtra(viewModel: AccountSetupViewModel) {
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CountryPickerBasicTextField(
            mobileNumber = viewModel.phone,
            defaultCountryCode = "ng",
            onMobileNumberChange = { viewModel.phone = it },
            onCountrySelected = {
                selectedCountry = it
                viewModel.selectedCountryCode = it.countryPhoneCode
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mobile Number") }
        )

        if (selectedCountry != null) {
            Text("Selected Country: ${selectedCountry!!.countryName} (${selectedCountry!!.countryPhoneCode})")
        }
    }
}
