package com.gibson.spica.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Handles local saving and cloud uploads for SPICA files.
 * Supports images, videos, and documents.
 * Compatible with Compose Multiplatform (Android-first now).
 */
class FileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // 🔹 UI State
    var isUploading by mutableStateOf(false)
        private set
    var uploadMessage by mutableStateOf<String?>(null)
        private set
    var downloadUrl by mutableStateOf<String?>(null)
        private set

    private val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    // =============================================================
    // 🖼️ Save File Locally
    // =============================================================
    private fun saveFileLocally(context: Context, uri: Uri, folder: String = "SPICA"): File? {
        return try {
            val resolver = context.contentResolver
            val fileName = getFileName(resolver, uri) ?: "SPICA_${formatter.format(Date())}"
            val input = resolver.openInputStream(uri) ?: return null

            val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), folder)
            if (!dir.exists()) dir.mkdirs()

            val destFile = File(dir, fileName)
            FileOutputStream(destFile).use { output ->
                copyStream(input, output)
            }
            destFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // =============================================================
    // ☁️ Upload File to Firebase Storage + Firestore metadata
    // =============================================================
    fun uploadFile(context: Context, uri: Uri, type: String = "file") {
        val safeContext = context.applicationContext

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isUploading = true
                    uploadMessage = "Preparing file..."
                }

                val user = auth.currentUser ?: throw Exception("User not logged in.")
                val localFile = saveFileLocally(safeContext, uri, folder = "SPICA_Uploads")
                    ?: throw Exception("Failed to save file locally.")

                val storageRef = storage.reference
                    .child("uploads/${user.uid}/${formatter.format(Date())}_${localFile.name}")

                withContext(Dispatchers.Main) { uploadMessage = "Uploading..." }

                storageRef.putFile(Uri.fromFile(localFile)).await()

                val url = storageRef.downloadUrl.await().toString()

                val meta = mapOf(
                    "uid" to user.uid,
                    "fileName" to localFile.name,
                    "type" to type,
                    "size" to localFile.length(),
                    "url" to url,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("uploads").add(meta).await()

                withContext(Dispatchers.Main) {
                    downloadUrl = url
                    uploadMessage = "Upload successful!"
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uploadMessage = "Upload failed: ${e.localizedMessage ?: "Unknown error"}"
                }
            } finally {
                withContext(Dispatchers.Main) { isUploading = false }
            }
        }
    }

    // =============================================================
    // 🧹 Delete Remote File
    // =============================================================
    fun deleteFileRemote(fileUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    isUploading = true
                    uploadMessage = "Deleting..."
                }
                val ref = storage.getReferenceFromUrl(fileUrl)
                ref.delete().await()
                withContext(Dispatchers.Main) {
                    uploadMessage = "File deleted successfully."
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uploadMessage = "Failed to delete: ${e.localizedMessage}"
                }
            } finally {
                withContext(Dispatchers.Main) { isUploading = false }
            }
        }
    }

    // =============================================================
    // 🧰 Helpers
    // =============================================================
    private fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

    private fun getFileName(resolver: ContentResolver, uri: Uri): String? {
        var name: String? = null
        val cursor = resolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex >= 0) name = it.getString(nameIndex)
        }
        return name
    }

    fun loadBitmapFromFile(file: File): Bitmap? = try {
        BitmapFactory.decodeFile(file.absolutePath)
    } catch (_: Exception) {
        null
    }
}
