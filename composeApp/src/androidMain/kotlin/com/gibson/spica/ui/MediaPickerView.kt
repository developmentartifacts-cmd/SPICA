package com.gibson.spica.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.gibson.spica.model.PlatformFile
import com.gibson.spica.viewmodel.MediaControllerViewModel

@Composable
fun AndroidMediaPicker(
    viewModel: MediaControllerViewModel,
    mimeFilter: String = "*/*"
) {
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val name = uri.lastPathSegment ?: "Unknown"
            val type = mimeFilter
            val file = PlatformFile(
                uri = uri.toString(),
                name = name,
                type = type,
                size = 0L,
                extension = name.substringAfterLast('.', "")
            )
            viewModel.onFileSelected(file)
        }
    }

    picker.launch(mimeFilter)
}
