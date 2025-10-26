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
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import com.gibson.spica.viewmodel.AccountSetupViewModel
import network.chaintech.cmpcountrycodepicker.component.CountryPickerBasicTextField
import network.chaintech.cmpcountrycodepicker.data.CountryDetails

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Step ${viewModel.currentStep} of ${viewModel.totalSteps}",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        when (viewModel.currentStep) {
            1 -> StepNames(viewModel)
            2 -> StepBio(viewModel)
            3 -> StepPhoneExtra(viewModel)
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (viewModel.currentStep > 1) {
                Button(onClick = { viewModel.previousStep() }) { Text("Back") }
            }

            if (viewModel.currentStep < viewModel.totalSteps) {
                Button(onClick = { viewModel.nextStep() }) { Text("Next") }
            } else {
                Button(onClick = { showDialog = true }) { Text("Finish") }
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "⚠️ $errorMessage", color = MaterialTheme.colorScheme.error)
        }
    }

    // ✅ Confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    viewModel.submitAccountSetup(
                        onSuccess = { Router.navigate(Screen.AccountSetupSuccess.route) },
                        onError = { errorMessage = it }
                    )
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Confirm Setup") },
            text = { Text("Do you want to complete your account setup?") }
        )
    }
}

@Composable
private fun StepNames(viewModel: AccountSetupViewModel) {
    OutlinedTextField(
        value = viewModel.firstName,
        onValueChange = { viewModel.firstName = it },
        label = { Text("First Name") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = viewModel.middleName,
        onValueChange = { viewModel.middleName = it },
        label = { Text("Middle Name") },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = viewModel.lastName,
        onValueChange = { viewModel.lastName = it },
        label = { Text("Last Name") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StepBio(viewModel: AccountSetupViewModel) {
    OutlinedTextField(
        value = viewModel.bio,
        onValueChange = { viewModel.bio = it },
        label = { Text("Bio") },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        maxLines = 5
    )
}

@Composable
private fun StepPhoneExtra(viewModel: AccountSetupViewModel) {
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }

    CountryPickerBasicTextField(
        mobileNumber = viewModel.mobileNumber,
        defaultCountryCode = "ng",
        onMobileNumberChange = { viewModel.mobileNumber = it },
        onCountrySelected = { country ->
            selectedCountry = country
            viewModel.countryName = country.countryName
            viewModel.countryCode = country.countryPhoneCode
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Mobile Number") },
        showCountryFlag = true,
        showCountryPhoneCode = true,
        showCountryName = true,
        showCountryCode = false,
        showArrowDropDown = true
    )

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "Selected: ${selectedCountry?.countryName ?: viewModel.countryName.ifEmpty { "None" }}",
        fontSize = 14.sp
    )
}
