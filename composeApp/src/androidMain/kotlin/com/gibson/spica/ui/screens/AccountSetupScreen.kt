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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel = viewModel()) {
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage
    val success = viewModel.successMessage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "Account Setup",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = viewModel.firstName,
                onValueChange = { viewModel.firstName = it },
                label = { Text("First Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.secondName,
                onValueChange = { viewModel.secondName = it },
                label = { Text("Second Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.lastName,
                onValueChange = { viewModel.lastName = it },
                label = { Text("Last Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                label = { Text("Username *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            DropdownSelector(
                label = "Country *",
                items = viewModel.countries,
                selected = viewModel.country,
                onSelect = { viewModel.onCountryChange(it) }
            )

            Spacer(Modifier.height(8.dp))

            val stateList = viewModel.states[viewModel.country] ?: emptyList()
            DropdownSelector(
                label = "State *",
                items = stateList,
                selected = viewModel.state,
                onSelect = { viewModel.onStateChange(it) },
                enabled = viewModel.country.isNotBlank()
            )

            Spacer(Modifier.height(8.dp))

            val towns = viewModel.towns[viewModel.state] ?: emptyList()
            DropdownSelectorPair(
                label = "Town/City *",
                items = towns,
                selected = viewModel.town,
                onSelect = { name, code -> viewModel.onTownChange(name, code) },
                enabled = viewModel.state.isNotBlank()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.postcode,
                onValueChange = { viewModel.postcode = it },
                label = { Text("Postcode *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.phoneNumber,
                onValueChange = { viewModel.phoneNumber = it },
                label = { Text("Phone Number") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            if (error != null) {
                Text(error, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(8.dp))
            }

            if (success != null) {
                Text(success, color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.saveProfile() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoading) "Saving..." else "Save & Continue")
            }
        }
    }
}

@Composable
fun DropdownSelector(
    label: String,
    items: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled) { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
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

@Composable
fun DropdownSelectorPair(
    label: String,
    items: List<Pair<String, String>>,
    selected: String,
    onSelect: (String, String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled) { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { (town, code) ->
                DropdownMenuItem(
                    text = { Text("$town ($code)") },
                    onClick = {
                        onSelect(town, code)
                        expanded = false
                    }
                )
            }
        }
    }
}
