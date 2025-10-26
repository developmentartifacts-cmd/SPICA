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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.navigation.Router
import com.gibson.spica.navigation.Screen

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel = remember { AccountSetupViewModel() }) {
    var currentStep by remember { mutableStateOf(1) }
    var showDialog by remember { mutableStateOf(false) }

    val state = viewModel.state
    val totalSteps = 3

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
            StepHeader(currentStep = currentStep, totalSteps = totalSteps, title = "Account Setup")

            Spacer(Modifier.height(16.dp))

            when (currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepLocation(viewModel)
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
fun StepLocation(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        SearchableDropdown(
            label = "State *",
            options = viewModel.getStates(),
            selected = state.state,
            onSelect = { viewModel.updateState(it) }
        )

        SearchableDropdown(
            label = "Town / LGA *",
            options = viewModel.getTowns(state.state),
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
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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

@Composable
fun SearchableDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredOptions = options.filter { it.contains(searchQuery, ignoreCase = true) }

    Column {
        OutlinedTextField(
            value = if (selected.isEmpty()) searchQuery else selected,
            onValueChange = {
                searchQuery = it
                expanded = true
            },
            label = { Text(label) },
            readOnly = selected.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 200.dp) // Scrollable
        ) {
            filteredOptions.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelect(item)
                        searchQuery = ""
                        expanded = false
                    }
                )
            }
        }
    }
}
