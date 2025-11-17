package com.gibson.spica.viewmodel

import com.gibson.spica.data.model.KmpFile
import com.gibson.spica.data.repository.FileRepository
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class FileState(
    val selectedFile: KmpFile? = null,
    val isUploading: Boolean = false,
    val uploadProgress: Float = 0f, // 0.0 to 1.0
    val downloadUrl: String? = null,
    val errorMessage: String? = null
)

class FileViewModel(
    private val fileRepository: FileRepository // 💡 Injected KMP Repository
) : ViewModel() { 

    private val _state = MutableStateFlow(FileState())
    val state: StateFlow<FileState> = _state.asStateFlow()

    private fun updateState(block: (FileState) -> FileState) {
        _state.value = block(_state.value)
    }

    /**
     * Triggers the file selection dialog using the abstracted repository.
     */
    fun pickFile(mimeTypes: List<String> = listOf("*/*")) {
        viewModelScope.launch {
            updateState { it.copy(selectedFile = null, errorMessage = null) }
            val file = fileRepository.selectFileForUpload(mimeTypes)
            updateState { it.copy(selectedFile = file) }
        }
    }

    /**
     * Starts the upload process for the currently selected file.
     */
    fun startUpload(storagePath: String) {
        val file = _state.value.selectedFile ?: run {
            updateState { it.copy(errorMessage = "No file selected for upload.") }
            return
        }

        updateState { it.copy(isUploading = true, uploadProgress = 0f, downloadUrl = null, errorMessage = null) }

        fileRepository.uploadFile(file, storagePath)
            .onEach { result ->
                result.onSuccess { url ->
                    if (url.startsWith("http")) { // Completed upload (URL is final result)
                        updateState { it.copy(downloadUrl = url, isUploading = false, uploadProgress = 1f) }
                    } else { // Progress update (URL carries progress % as a string/float)
                        updateState { it.copy(uploadProgress = url.toFloatOrNull() ?: 0f) }
                    }
                }.onFailure { e ->
                    updateState { it.copy(errorMessage = e.localizedMessage, isUploading = false) }
                }
            }
            .launchIn(viewModelScope)
    }
}
