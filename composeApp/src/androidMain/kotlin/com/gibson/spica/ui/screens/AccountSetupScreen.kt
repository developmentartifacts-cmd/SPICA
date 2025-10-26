package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    var currentStep by remember { mutableStateOf(1) }
    val totalSteps = 3
    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StepHeader(currentStep = currentStep, totalSteps = totalSteps, title = when (currentStep) {
                    1 -> "Personal Information"
                    2 -> "Location Information"
                    3 -> "Contact & Bio"
                    else -> ""
                })

                when (currentStep) {
                    1 -> StepNames(viewModel)
                    2 -> StepLocation(viewModel)
                    3 -> StepPhoneExtra(viewModel)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (currentStep > 1) {
                        OutlinedButton(onClick = { currentStep-- }) { Text("Back") }
                    } else {
                        Spacer(modifier = Modifier.width(80.dp))
                    }

                    Button(onClick = {
                        if (currentStep < totalSteps) {
                            currentStep++
                        } else {
                            showConfirmDialog = true
                        }
                    }) {
                        Text(if (currentStep < totalSteps) "Next" else "Finish")
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirm Save") },
            text = { Text("Do you want to save your account information?") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    viewModel.submitAccountSetup()
                    Router.navigate(Screen.AccountSetupSuccess.route)
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun StepNames(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
fun StepLocation(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        DropdownMenuBox(
            label = "Country *",
            options = viewModel.countryList,
            selected = state.country,
            onSelect = { viewModel.updateCountry(it) }
        )
        DropdownMenuBox(
            label = "State *",
            options = viewModel.getStatesForCountry(state.country),
            selected = state.state,
            onSelect = { viewModel.updateState(it) }
        )
        DropdownMenuBox(
            label = "Town / City *",
            options = viewModel.getTownsForState(state.state),
            selected = state.town,
            onSelect = { viewModel.updateTown(it) }
        )
        OutlinedTextField(
            value = state.postcode,
            onValueChange = { viewModel.updatePostcode(it) },
            label = { Text("Postcode *") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun StepPhoneExtra(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = state.phone,
            onValueChange = { viewModel.updatePhone(it) },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
        OutlinedTextField(
            value = state.bio,
            onValueChange = { viewModel.updateBio(it) },
            label = { Text("Bio (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
