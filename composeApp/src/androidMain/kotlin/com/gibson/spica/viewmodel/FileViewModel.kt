package com.gibson.spica.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Handles file saving & uploading for SPICA.
 * Works with images, videos, and documents — no Coil/Glide.
 */
class FileViewModel : ViewModel() {

    // 🔹 Firebase instances
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // 🔹 UI observable states
    var isUploading by androidx.compose.runtime.mutableStateOf(false)
        private set
    var uploadMessage by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set
    var downloadUrl by androidx.compose.runtime.mutableStateOf<String?>(null)
        private set

    private val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())

    // =============================================================
    // 🖼️ Save File Locally
    // =============================================================

    fun saveFileLocally(context: Context, uri: Uri, folder: String = "SPICA"): File? {
        return try {
            val resolver: ContentResolver = context.contentResolver
            val fileName = getFileName(resolver, uri) ?: "SPICA_${formatter.format(Date())}"
            val input = resolver.openInputStream(uri) ?: return null

            val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), folder)
            if (!dir.exists()) dir.mkdirs()

            val destFile = File(dir, fileName)
            val output = FileOutputStream(destFile)
            copyStream(input, output)
            destFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // =============================================================
    // ☁️ Upload to Firebase Storage + Firestore metadata
    // =============================================================

    fun uploadFile(context: Context, uri: Uri, type: String = "file") {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isUploading = true
                uploadMessage = "Preparing file..."

                val user = auth.currentUser ?: throw Exception("User not logged in.")
                val file = saveFileLocally(context, uri, folder = "SPICA_Uploads")
                    ?: throw Exception("Failed to save file locally.")

                val storageRef = storage.reference
                    .child("uploads/${user.uid}/${formatter.format(Date())}_${file.name}")

                val uploadTask = storageRef.putFile(Uri.fromFile(file)).await()
                val url = storageRef.downloadUrl.await().toString()

                // 🔸 Save metadata to Firestore
                val meta = mapOf(
                    "uid" to user.uid,
                    "fileName" to file.name,
                    "type" to type,
                    "size" to file.length(),
                    "url" to url,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("uploads").add(meta).await()

                isUploading = false
                uploadMessage = "Upload successful!"
                downloadUrl = url

            } catch (e: Exception) {
                e.printStackTrace()
                isUploading = false
                uploadMessage = "Error: ${e.message}"
            }
        }
    }

    // =============================================================
    // 🧹 Delete File (local & remote)
    // =============================================================

    fun deleteFileRemote(fileUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isUploading = true
                uploadMessage = "Deleting file..."
                val ref = storage.getReferenceFromUrl(fileUrl)
                ref.delete().await()
                isUploading = false
                uploadMessage = "File deleted successfully."
            } catch (e: Exception) {
                isUploading = false
                uploadMessage = "Failed to delete: ${e.message}"
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
        output.flush()
        input.close()
        output.close()
    }

    private fun getFileName(resolver: ContentResolver, uri: Uri): String? {
        var name: String? = null
        val cursor = resolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst() && nameIndex >= 0) {
                name = it.getString(nameIndex)
            }
        }
        return name
    }

    fun loadBitmapFromFile(file: File): Bitmap? {
        return try {
            BitmapFactory.decodeFile(file.absolutePath)
        } catch (e: Exception) {
            null
        }
    }
}
