package com.gibson.spica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibson.spica.media.PlatformFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/**
 * Handles picking and uploading of media (images, videos, files).
 * Universal ViewModel usable from any screen.
 */
class MediaManagerViewModel : ViewModel() {

    private val _selectedFile = MutableStateFlow<PlatformFile?>(null)
    val selectedFile = _selectedFile.asStateFlow()

    private val _uploadProgress = MutableStateFlow<Float?>(null)
    val uploadProgress = _uploadProgress.asStateFlow()

    private val _downloadUrl = MutableStateFlow<String?>(null)
    val downloadUrl = _downloadUrl.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance().reference

    fun onFileSelected(file: PlatformFile?) {
        _selectedFile.value = file
        _uploadProgress.value = null
        _downloadUrl.value = null
    }

    fun clear() {
        _selectedFile.value = null
        _uploadProgress.value = null
        _downloadUrl.value = null
    }

    /**
     * Upload selected file to Firebase Storage at /uploads/{uid}/{customName or file.name}
     */
    fun uploadSelectedFile(customName: String? = null) {
        val user = auth.currentUser ?: return
        val file = _selectedFile.value ?: return

        viewModelScope.launch {
            try {
                val path = "uploads/${user.uid}/${customName ?: file.name}"
                val ref = storage.child(path)

                val uploadTask = ref.putBytes(file.bytes)
                uploadTask.addOnProgressListener {
                    val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toFloat()
                    _uploadProgress.value = progress
                }

                uploadTask.await() // suspend until done
                val url = ref.downloadUrl.await().toString()
                _downloadUrl.value = url
                _uploadProgress.value = 100f

            } catch (e: Exception) {
                e.printStackTrace()
                _uploadProgress.value = null
                _downloadUrl.value = null
            }
        }
    }
}
