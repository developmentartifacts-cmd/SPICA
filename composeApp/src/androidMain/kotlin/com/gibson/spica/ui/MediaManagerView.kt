package com.gibson.spica.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gibson.spica.util.readPlatformFileFromUri
import com.gibson.spica.viewmodel.MediaManagerViewModel
import kotlin.math.roundToInt

/**
 * Simple UI: Pick a file → Upload to Firebase → Show URL & progress
 */
@Composable
fun MediaManagerView(
    viewModel: MediaManagerViewModel = viewModel(),
    label: String = "Pick File",
    customFileName: String? = null
) {
    val context = LocalContext.current
    val selectedFile by viewModel.selectedFile.collectAsState()
    val uploadProgress by viewModel.uploadProgress.collectAsState()
    val downloadUrl by viewModel.downloadUrl.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = readPlatformFileFromUri(context, it)
            viewModel.onFileSelected(file)
        }
    }

    Column(Modifier.fillMaxWidth().padding(8.dp)) {
        Row {
            Button(onClick = { launcher.launch("*/*") }) {
                Text(label)
            }
            Spacer(Modifier.width(8.dp))
            if (selectedFile != null) {
                OutlinedButton(onClick = { viewModel.uploadSelectedFile(customFileName) }) {
                    Text("Upload")
                }
            }
        }

        uploadProgress?.let { progress ->
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = (progress / 100f).coerceIn(0f, 1f),
                modifier = Modifier.fillMaxWidth()
            )
            Text("Uploading: ${progress.roundToInt()}%")
        }

        downloadUrl?.let { url ->
            Spacer(Modifier.height(8.dp))
            Text("✅ Uploaded: $url")
        }
    }
}
