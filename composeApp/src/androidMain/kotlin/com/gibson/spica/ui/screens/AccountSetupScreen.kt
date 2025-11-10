package com.gibson.spica.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gibson.spica.data.CountryCodeData
import com.gibson.spica.data.LocationData
import com.gibson.spica.ui.components.CountryCodePicker
import com.gibson.spica.ui.components.CountryPicker
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.viewmodel.FileViewModel

@Composable
fun AccountSetupScreen(
    viewModel: AccountSetupViewModel = AccountSetupViewModel(),
    fileVM: FileViewModel = FileViewModel()
) {
    val state = viewModel.state

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Header
            Text("Complete your profile", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(12.dp))

            // Step content
            when (state.step) {
                1 -> StepName(viewModel)
                2 -> StepLocation(viewModel)
                3 -> StepBioPhone(viewModel)
                4 -> StepPhotos(viewModel, fileVM)
            }

            Spacer(Modifier.height(16.dp))

            // Bottom controls: Back / Next / Finish
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (state.step > 1) {
                    OutlinedButton(
                        onClick = { viewModel.prevStep() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Back")
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                val primaryLabel = if (state.step == 4) "Finish" else "Next"
                Button(
                    onClick = {
                        if (state.step == 4) {
                            viewModel.completeSetup()
                        } else {
                            viewModel.nextStep()
                        }
                    },
                    enabled = !state.isLoading,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(primaryLabel)
                }
            }

            state.message?.let {
                Spacer(Modifier.height(12.dp))
                Text(it, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun StepName(viewModel: AccountSetupViewModel) {
    var name by remember { mutableStateOf(viewModel.state.displayName) }

    Column {
        Surface(shape = RoundedCornerShape(50), tonalElevation = 0.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Step 1: Your Name", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = {
                viewModel.updateDisplayName(name)
                viewModel.nextStep()
            }, shape = RoundedCornerShape(50)) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun StepLocation(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    var showCountryDialog by remember { mutableStateOf(false) }
    var showRegionDialog by remember { mutableStateOf(false) }

    val selectedIso = state.countryIso
    val selectedCountryName = state.countryName ?: CountryCodeData.findByIso(selectedIso ?: "")?.name

    Column {
        Surface(shape = RoundedCornerShape(50), tonalElevation = 0.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Step 2: Location", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                // Country selector (modal)
                Text("Country", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showCountryDialog = true }
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(selectedCountryName ?: "Select country")
                        Spacer(Modifier.weight(1f))
                        Text("▾")
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Region/State/Town selector (depends on selected country)
                Text("Region / State / Town", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // only show region selector if country selected
                            if (state.countryIso != null) showRegionDialog = true
                        }
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(state.region ?: "Select region / state / town")
                        Spacer(Modifier.weight(1f))
                        Text("▾")
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // Country modal dialog
        if (showCountryDialog) {
            AlertDialog(
                onDismissRequest = { showCountryDialog = false },
                title = { Text("Select country") },
                text = {
                    Column(Modifier.fillMaxWidth()) {
                        LocationData.countries.forEach { cName ->
                            // For each country, attempt to map to iso via CountryCodeData.list (best-effort)
                            val iso = CountryCodeData.list.find { it.name == cName }?.iso
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        // update iso + country name
                                        viewModel.updateCountry(iso ?: cName.take(2).uppercase(), cName)
                                        showCountryDialog = false
                                    }
                                    .padding(vertical = 12.dp)
                            ) {
                                Text(cName)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCountryDialog = false }) { Text("Close") }
                }
            )
        }

        // Region modal dialog
        if (showRegionDialog) {
            val statesMap = LocationData.getStatesForCountry(state.countryName ?: "")
            val entries = if (statesMap.isNotEmpty()) statesMap.entries.flatMap { (k, v) -> listOf(k) + v } else emptyList()
            AlertDialog(
                onDismissRequest = { showRegionDialog = false },
                title = { Text("Select region / state / town") },
                text = {
                    Column(Modifier.fillMaxWidth()) {
                        if (entries.isEmpty()) {
                            Text("No regions available for selected country")
                        } else {
                            entries.forEach { item ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.updateRegion(item)
                                            showRegionDialog = false
                                        }
                                        .padding(vertical = 12.dp)
                                ) {
                                    Text(item)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showRegionDialog = false }) { Text("Close") }
                }
            )
        }

        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = { viewModel.nextStep() }, shape = RoundedCornerShape(50)) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun StepBioPhone(viewModel: AccountSetupViewModel) {
    val state = viewModel.state
    var phone by remember { mutableStateOf(state.phone) }
    var bio by remember { mutableStateOf(state.bio) }

    Column {
        Surface(shape = RoundedCornerShape(50), tonalElevation = 0.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Step 3: Bio & Contact", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                // Dial code + phone
                Text("Phone", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CountryCodePicker(selectedDialCode = state.dialCode, onDialCodeSelected = { viewModel.updateDialCode(it) })
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text("Bio", style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Short bio") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 4
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = {
                viewModel.updateBioAndPhone(phone, bio)
                viewModel.nextStep()
            }, shape = RoundedCornerShape(50)) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun StepPhotos(viewModel: AccountSetupViewModel, fileVM: FileViewModel) {
    val context = LocalContext.current
    var profileUri by remember { mutableStateOf<Uri?>(null) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }

    val profilePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> profileUri = uri }
    val coverPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> coverUri = uri }

    Column {
        Surface(shape = RoundedCornerShape(50), tonalElevation = 0.dp, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("Step 4: Profile & Cover Photos", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))

                Button(onClick = { profilePicker.launch("image/*") }, shape = RoundedCornerShape(50)) {
                    Text("Select Profile Picture")
                }
                Spacer(Modifier.height(8.dp))
                Button(onClick = { coverPicker.launch("image/*") }, shape = RoundedCornerShape(50)) {
                    Text("Select Cover Photo")
                }

                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        profileUri?.let { fileVM.uploadFile(context, it, "profile") }
                        coverUri?.let { fileVM.uploadFile(context, it, "cover") }
                    },
                    enabled = !fileVM.isUploading,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(if (fileVM.isUploading) "Uploading..." else "Upload Files")
                }

                fileVM.uploadMessage?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Row {
            Button(onClick = { viewModel.completeSetup() }, shape = RoundedCornerShape(50)) {
                Text("Finish")
            }
        }
    }
}
