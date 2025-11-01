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
import com.gibson.spica.util.readPlatformFileFromUri

/**
 * Simplified media picker — picks file and returns raw bytes.
 * No preview, no upload progress UI.
 */
@Composable
fun MediaPickerViewSimple(
    label: String,
    isUploading: Boolean,
    uploaded: Boolean,
    onFileSelected: (ByteArray) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = readPlatformFileFromUri(context, it)
            file?.let { onFileSelected(it.bytes) }
        }
    }

    Column {
        Button(
            onClick = { launcher.launch("image/*") },
            enabled = !isUploading
        ) {
            Text(
                when {
                    isUploading -> "Uploading..."
                    uploaded -> "✅ $label (Done)"
                    else -> label
                }
            )
        }
    }
}
