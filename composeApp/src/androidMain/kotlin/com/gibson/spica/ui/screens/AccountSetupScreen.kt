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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import network.chaintech.cmpcountrycodepicker.CountryPickerBasicTextField
import network.chaintech.cmpcountrycodepicker.countrycode.model.CountryDetails

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel = remember { AccountSetupViewModel() }) {
    val currentStep = viewModel.currentStep
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            StepHeader(
                currentStep = currentStep,
                totalSteps = 3,
                title = when (currentStep) {
                    1 -> "Your Names"
                    2 -> "Bio Information"
                    3 -> "Phone & Country"
                    else -> ""
                }
            )

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
                    OutlinedButton(onClick = { viewModel.prevStep() }) {
                        Text("Back")
                    }
                }
                Button(
                    onClick = {
                        if (currentStep < 3) viewModel.nextStep()
                        else viewModel.showDialog = true
                    }
                ) {
                    Text(if (currentStep < 3) "Next" else "Finish")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            viewModel.message?.let {
                Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        // Confirmation dialog
        if (viewModel.showDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.showDialog = false },
                title = { Text("Confirm Submission") },
                text = { Text("Do you want to save your account setup?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.submitAccountSetup {
                            Router.navigate(Screen.AccountSetupSuccess.route)
                        }
                    }) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
private fun StepHeader(currentStep: Int, totalSteps: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text("Step $currentStep of $totalSteps", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun StepNames(viewModel: AccountSetupViewModel) {
    OutlinedTextField(
        value = viewModel.firstName,
        onValueChange = { viewModel.firstName = it },
        label = { Text("First Name *") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    OutlinedTextField(
        value = viewModel.secondName,
        onValueChange = { viewModel.secondName = it },
        label = { Text("Second Name") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    OutlinedTextField(
        value = viewModel.lastName,
        onValueChange = { viewModel.lastName = it },
        label = { Text("Last Name *") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
    OutlinedTextField(
        value = viewModel.username,
        onValueChange = { viewModel.username = it },
        label = { Text("Username *") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun StepBio(viewModel: AccountSetupViewModel) {
    OutlinedTextField(
        value = viewModel.bio,
        onValueChange = { viewModel.bio = it },
        label = { Text("Bio / About You") },
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun StepPhoneExtra(viewModel: AccountSetupViewModel) {
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }

    CountryPickerBasicTextField(
        mobileNumber = viewModel.mobileNumber,
        defaultCountryCode = "ng",
        onMobileNumberChange = { viewModel.mobileNumber = it },
        onCountrySelected = {
            selectedCountry = it
            viewModel.countryName = it.countryName
            viewModel.countryCode = it.countryPhoneCode
        },
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Mobile Number") },
        showCountryFlag = true,
        showCountryPhoneCode = true,
        showCountryName = true
    )

    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = "Selected Country: ${selectedCountry?.countryName ?: viewModel.countryName.ifEmpty { "None" }}",
        fontSize = 14.sp
    )
}
