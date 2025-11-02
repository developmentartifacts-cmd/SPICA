@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.viewmodel.MediaControllerViewModel
import com.gibson.spica.ui.MediaControllerView
import com.gibson.spica.ui.AndroidMediaPicker

@Composable
fun AccountSetupScreen(viewModel: AccountSetupViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when (viewModel.currentStep.value) {
            1 -> StepNames(viewModel)
            2 -> StepPhoneBio(viewModel)
            3 -> StepLocation(viewModel)
            4 -> StepMedia(viewModel)
        }

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (viewModel.currentStep.value > 1) {
                Button(onClick = { viewModel.prevStep() }) { Text("Back") }
            }

            Button(onClick = { viewModel.nextStep() }) {
                Text(if (viewModel.currentStep.value < 4) "Next" else "Finish")
            }
        }
    }
}

@Composable
fun StepNames(viewModel: AccountSetupViewModel) {
    Column {
        TextField(value = viewModel.firstName.value, onValueChange = { viewModel.firstName.value = it }, label = { Text("First Name") })
        Spacer(Modifier.height(8.dp))
        TextField(value = viewModel.middleName.value, onValueChange = { viewModel.middleName.value = it }, label = { Text("Middle Name") })
        Spacer(Modifier.height(8.dp))
        TextField(value = viewModel.lastName.value, onValueChange = { viewModel.lastName.value = it }, label = { Text("Last Name") })
    }
}

@Composable
fun StepPhoneBio(viewModel: AccountSetupViewModel) {
    Column {
        TextField(value = viewModel.phoneNumber.value, onValueChange = { viewModel.phoneNumber.value = it }, label = { Text("Phone Number") })
        Spacer(Modifier.height(8.dp))
        TextField(value = viewModel.bio.value, onValueChange = { viewModel.bio.value = it }, label = { Text("Bio") })
    }
}

@Composable
fun StepLocation(viewModel: AccountSetupViewModel) {
    Column {
        DropdownSelector(
            label = "Country",
            items = com.gibson.spica.data.LocationData.countries,
            selected = viewModel.selectedCountry.value,
            onSelected = { viewModel.onCountrySelected(it) }
        )

        if (viewModel.availableStates.value.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            DropdownSelector(
                label = "State / Region",
                items = viewModel.availableStates.value,
                selected = viewModel.selectedState.value,
                onSelected = { viewModel.onStateSelected(it) }
            )
        }

        if (viewModel.availableTowns.value.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            DropdownSelector(
                label = "Town / City",
                items = viewModel.availableTowns.value,
                selected = viewModel.selectedTown.value,
                onSelected = { viewModel.onTownSelected(it) }
            )
        }
    }
}

@Composable
fun StepMedia(viewModel: AccountSetupViewModel) {
    val profilePicker = remember { MediaControllerViewModel() }
    val coverPicker = remember { MediaControllerViewModel() }

    Column {
        Text("Profile Photo")
        Spacer(Modifier.height(8.dp))
        MediaControllerView(
            viewModel = profilePicker,
            onPickFile = { AndroidMediaPicker(profilePicker) }
        )

        Spacer(Modifier.height(16.dp))
        Text("Cover Photo")
        Spacer(Modifier.height(8.dp))
        MediaControllerView(
            viewModel = coverPicker,
            onPickFile = { AndroidMediaPicker(coverPicker) }
        )

        viewModel.profilePhoto.value = profilePicker.selectedFile.value
        viewModel.coverPhoto.value = coverPicker.selectedFile.value
    }
}

@Composable
fun DropdownSelector(
    label: String,
    items: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { expanded = !expanded }
        ) { Text(if (selected.isEmpty()) label else selected) }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach {
                DropdownMenuItem(text = { Text(it) }, onClick = {
                    onSelected(it)
                    expanded = false
                })
            }
        }
    }
}
