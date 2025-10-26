package com.gibson.spica.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen
import network.chaintech.countrypicker.model.CountryDetails
import network.chaintech.countrypicker.CountryPickerBasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.interaction.MutableInteractionSource

@Composable
fun AccountSetupScreen() {
    val context = LocalContext.current
    val viewModel = remember { AccountSetupViewModel(context) }

    var currentStep by remember { mutableStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }
    val totalSteps = 3

    val state = viewModel.state

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StepHeader(currentStep, totalSteps, "Account Setup")

            Spacer(Modifier.height(16.dp))

            when (currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepPhoneExtra(viewModel)
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (currentStep > 1) {
                    OutlinedButton(onClick = { currentStep-- }) {
                        Text("Back")
                    }
                }

                Button(
                    onClick = {
                        if (currentStep < totalSteps) {
                            currentStep++
                        } else {
                            showDialog = true
                        }
                    },
                    enabled = !state.isLoading
                ) {
                    Text(if (currentStep < totalSteps) "Next" else "Finish")
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Confirm Save") },
                text = { Text("Are you sure you want to complete your account setup?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        viewModel.submitAccountSetup {
                            Router.navigate(Screen.AccountSetupSuccess.route)
                        }
                    }) {
                        Text("Yes, Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun StepHeader(currentStep: Int, totalSteps: Int, title: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(title, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text("Step $currentStep of $totalSteps", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun StepNames(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.firstName,
            onValueChange = { viewModel.updateFirstName(it) },
            label = { Text("First Name *") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.secondName,
            onValueChange = { viewModel.updateSecondName(it) },
            label = { Text("Second Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.lastName,
            onValueChange = { viewModel.updateLastName(it) },
            label = { Text("Last Name *") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.username,
            onValueChange = { viewModel.updateUsername(it) },
            label = { Text("Username *") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun StepBio(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        OutlinedTextField(
            value = state.bio,
            onValueChange = { viewModel.updateBio(it) },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.town,
            onValueChange = { viewModel.updateTown(it) },
            label = { Text("Town/City") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = state.postcode,
            onValueChange = { viewModel.updatePostcode(it) },
            label = { Text("Postcode") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun StepPhoneExtra(viewModel: AccountSetupViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        AccountPhoneInput(viewModel)
    }
}

@Composable
fun AccountPhoneInput(viewModel: AccountSetupViewModel) {
    var mobileNumber by remember { mutableStateOf(viewModel.state.phone) }
    var selectedCountry by remember { mutableStateOf<CountryDetails?>(null) }

    CountryPickerBasicTextField(
        mobileNumber = mobileNumber,
        defaultCountryCode = "ng",
        onMobileNumberChange = {
            mobileNumber = it
            viewModel.updatePhone(it)
        },
        onCountrySelected = { country ->
            selectedCountry = country
        },
        modifier = Modifier.fillMaxWidth(),
        defaultPaddingValues = PaddingValues(6.dp),
        showCountryFlag = true,
        showCountryPhoneCode = true,
        showArrowDropDown = true,
        label = { Text("Mobile Number") },
        focusedBorderThickness = 2.dp,
        unfocusedBorderThickness = 1.dp,
        shape = RoundedCornerShape(10.dp),
        verticalDividerColor = Color(0xFFDDDDDD),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFDDDDDD),
            unfocusedBorderColor = Color(0xFFDDDDDD)
        )
    )
}
