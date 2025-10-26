package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import kotlinx.coroutines.launch
import network.chaintech.cmpcountrycodepicker.components.CountryPickerBasicTextField
import network.chaintech.cmpcountrycodepicker.data.CountryData

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel = remember { AccountSetupViewModel() }) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 3

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            StepHeader(currentStep = currentStep, totalSteps = totalSteps)

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
                    OutlinedButton(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Back")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Button(
                    onClick = {
                        if (currentStep < totalSteps) {
                            currentStep++
                        } else {
                            coroutineScope.launch {
                                viewModel.saveUserData()
                                snackbarHostState.showSnackbar("Account setup completed!")
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (currentStep == totalSteps) "Finish" else "Next")
                }
            }
        }
    }
}

/* ---------------------- Step Header ---------------------- */
@Composable
private fun StepHeader(currentStep: Int, totalSteps: Int) {
    Text(
        text = "Step $currentStep of $totalSteps",
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
}

/* ---------------------- Step 1: Names ---------------------- */
@Composable
private fun StepNames(viewModel: AccountSetupViewModel) {
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
            label = { Text("Second Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Last Name *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.username,
            onValueChange = { viewModel.username = it },
            label = { Text("Username *") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* ---------------------- Step 2: Bio ---------------------- */
@Composable
private fun StepBio(viewModel: AccountSetupViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = viewModel.bio,
            onValueChange = { viewModel.bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = VisualTransformation.None,
            minLines = 3,
            maxLines = 5
        )

        OutlinedTextField(
            value = viewModel.postcode,
            onValueChange = { viewModel.postcode = it },
            label = { Text("Postcode") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

/* ---------------------- Step 3: Phone Picker ---------------------- */
@Composable
private fun StepPhoneExtra(viewModel: AccountSetupViewModel) {
    var selectedCountry by remember { mutableStateOf<CountryData?>(null) }

    CountryPickerBasicTextField(
        mobileNumber = viewModel.phone,
        defaultCountryCode = "ng",
        onMobileNumberChange = { viewModel.phone = it },
        onCountrySelected = { country: CountryData ->
            selectedCountry = country
            viewModel.countryName = country.name
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
        text = "Selected: ${selectedCountry?.name ?: viewModel.countryName.ifEmpty { "None" }}",
        fontSize = 14.sp
    )
}
