@file:OptIn(ExperimentalMaterial3Api::class)
package com.gibson.spica.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.gibson.spica.viewmodel.AccountSetupViewModel
import com.gibson.spica.viewmodel.FileViewModel

@Composable
fun AccountSetupScreen(
    viewModel: AccountSetupViewModel = remember { AccountSetupViewModel() },
    fileVM: FileViewModel = remember { FileViewModel() }
) {
    val state = viewModel.state
    val context = LocalContext.current

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            when (state.step) {
                1 -> StepName(viewModel)
                2 -> StepBioPhone(viewModel)
                3 -> StepLocation(viewModel)
                4 -> StepPhotos(viewModel, fileVM, context)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (state.step > 1) {
                    OutlinedButton(
                        onClick = { viewModel.prevStep() },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Back") }
                    Spacer(Modifier.height(8.dp))
                }

                Button(
                    onClick = {
                        if (state.step == 4) {
                            viewModel.submitProfile()
                        } else {
                            viewModel.nextStep()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text(
                        if (state.step == 4)
                            if (state.isLoading) "Saving..." else "Finish"
                        else "Next"
                    )
                }

                Spacer(Modifier.height(8.dp))

                state.message?.let {
                    Text(it, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun StepName(viewModel: AccountSetupViewModel) {
    var name by remember { mutableStateOf(viewModel.state.name) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 1: Your Name", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.updateName(name); viewModel.nextStep() }) {
            Text("Continue")
        }
    }
}

@Composable
fun StepBioPhone(viewModel: AccountSetupViewModel) {
    var phone by remember { mutableStateOf(viewModel.state.phone) }
    var bio by remember { mutableStateOf(viewModel.state.bio) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 2: Bio & Contact", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = phone, onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = bio, onValueChange = { bio = it },
            label = { Text("Bio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.updateBioAndPhone(phone, bio); viewModel.nextStep() }) {
            Text("Continue")
        }
    }
}

@Composable
fun StepLocation(viewModel: AccountSetupViewModel) {
    var location by remember { mutableStateOf(viewModel.state.location) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 3: Location", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Your Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.updateLocation(location); viewModel.nextStep() }) {
            Text("Continue")
        }
    }
}

@Composable
fun StepPhotos(
    viewModel: AccountSetupViewModel,
    fileVM: FileViewModel,
    context: android.content.Context
) {
    var profileUri by remember { mutableStateOf<Uri?>(null) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }

    val profilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> profileUri = uri }
    )
    val coverPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> coverUri = uri }
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Step 4: Profile & Cover Photos", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        Button(onClick = { profilePicker.launch("image/*") }) {
            Text("Select Profile Picture")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { coverPicker.launch("image/*") }) {
            Text("Select Cover Photo")
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                profileUri?.let { fileVM.uploadFile(context, it, "profile") }
                coverUri?.let { fileVM.uploadFile(context, it, "cover") }
            },
            enabled = !fileVM.isUploading
        ) {
            Text(if (fileVM.isUploading) "Uploading..." else "Upload Files")
        }

        fileVM.uploadMessage?.let {
            Spacer(Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        LaunchedEffect(fileVM.downloadUrl) {
            fileVM.downloadUrl?.let { url ->
                if (profileUri != null)
                    viewModel.saveProfileImages(url, viewModel.state.coverUrl)
                if (coverUri != null)
                    viewModel.saveProfileImages(viewModel.state.profileUrl, url)
            }
        }
    }
}
