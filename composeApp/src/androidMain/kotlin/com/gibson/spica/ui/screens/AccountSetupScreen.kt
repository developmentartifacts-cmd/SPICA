package com.gibson.spica.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel = remember { AccountSetupViewModel() }) {
    val state = viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Account Setup",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                // --- Name fields ---
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
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = {
                        if (state.usernameError != null)
                            Text(state.usernameError!!, color = MaterialTheme.colorScheme.error)
                    }
                )

                // --- Country, State, Town Dropdowns ---
                DropdownSelector(
                    label = "Country *",
                    options = viewModel.countryList,
                    selected = state.country,
                    onSelect = { viewModel.updateCountry(it) }
                )

                DropdownSelector(
                    label = "State *",
                    options = viewModel.getStatesForCountry(state.country),
                    selected = state.state,
                    onSelect = { viewModel.updateState(it) }
                )

                DropdownSelector(
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

                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { viewModel.updatePhone(it) },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.submitAccountSetup() },
                    enabled = !state.isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (state.isLoading) "Saving..." else "Continue")
                }

                TextButton(onClick = { viewModel.skipPhoneVerification() }) {
                    Text("Skip Phone Verification")
                }

                if (state.message != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(state.message!!, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            if (options.isEmpty()) {
                DropdownMenuItem(
                    text = { Text("No options available") },
                    onClick = { expanded = false }
                )
            } else {
                options.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onSelect(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
