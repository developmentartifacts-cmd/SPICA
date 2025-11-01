package com.gibson.spica.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gibson.spica.model.PlatformFile

class MediaControllerViewModel : ViewModel() {

    val selectedFile = mutableStateOf<PlatformFile?>(null)

    fun onFileSelected(file: PlatformFile?) {
        selectedFile.value = file
    }

    fun clearSelection() {
        selectedFile.value = null
    }
}
