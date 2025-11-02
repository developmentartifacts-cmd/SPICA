@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.data.LocationData
import com.gibson.spica.viewmodel.AccountSetupViewModel

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    val currentStep = viewModel.currentStep

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            when (currentStep) {
                1 -> StepNames(viewModel)
                2 -> StepBio(viewModel)
                3 -> StepLocation(viewModel)
            }
        }
    }
}

@Composable
fun StepNames(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 1 of 3: Names", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = { viewModel.firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = { viewModel.lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = { viewModel.nextStep() }, modifier = Modifier.fillMaxWidth()) {
            Text("Next")
        }
    }
}

@Composable
fun StepBio(viewModel: AccountSetupViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 2 of 3: Phone & Bio", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = viewModel.bio,
            onValueChange = { viewModel.bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { viewModel.previousStep() }) {
                Text("Back")
            }
            Button(onClick = { viewModel.nextStep() }) {
                Text("Next")
            }
        }
    }
}

@Composable
fun StepLocation(viewModel: AccountSetupViewModel) {
    var expandedCountry by remember { mutableStateOf(false) }
    var expandedState by remember { mutableStateOf(false) }
    var expandedTown by remember { mutableStateOf(false) }

    val countries = LocationData.countries
    val states = LocationData.getStatesForCountry(viewModel.country).keys.toList()
    val towns = LocationData.getStatesForCountry(viewModel.country)[viewModel.state] ?: emptyList()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 3 of 3: Location", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        // Country dropdown
        ExposedDropdownMenuBox(expanded = expandedCountry, onExpandedChange = { expandedCountry = it }) {
            OutlinedTextField(
                value = viewModel.country,
                onValueChange = {},
                readOnly = true,
                label = { Text("Country") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedCountry, onDismissRequest = { expandedCountry = false }) {
                countries.forEach { country ->
                    DropdownMenuItem(
                        text = { Text(country) },
                        onClick = {
                            viewModel.country = country
                            viewModel.state = ""
                            viewModel.town = ""
                            expandedCountry = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // State dropdown
        ExposedDropdownMenuBox(expanded = expandedState, onExpandedChange = { expandedState = it }) {
            OutlinedTextField(
                value = viewModel.state,
                onValueChange = {},
                readOnly = true,
                label = { Text("State/Region") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedState, onDismissRequest = { expandedState = false }) {
                states.forEach { state ->
                    DropdownMenuItem(
                        text = { Text(state) },
                        onClick = {
                            viewModel.state = state
                            viewModel.town = ""
                            expandedState = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Town dropdown
        ExposedDropdownMenuBox(expanded = expandedTown, onExpandedChange = { expandedTown = it }) {
            OutlinedTextField(
                value = viewModel.town,
                onValueChange = {},
                readOnly = true,
                label = { Text("Town") },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedTown, onDismissRequest = { expandedTown = false }) {
                towns.forEach { town ->
                    DropdownMenuItem(
                        text = { Text(town) },
                        onClick = {
                            viewModel.town = town
                            expandedTown = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = { viewModel.previousStep() }) {
                Text("Back")
            }
            Button(
                onClick = { viewModel.nextStep() },
                enabled = !viewModel.isLoading
            ) {
                Text(if (viewModel.isLoading) "Saving..." else "Finish")
            }
        }

        viewModel.message?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }
    }
}
