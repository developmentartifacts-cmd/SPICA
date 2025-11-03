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

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // 🔹 Observable UI states
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
    fun saveFileLocally(context: Context, uri: Uri, folder: String = "SPICA"): File? {
        return try {
            val resolver = context.contentResolver
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

                storageRef.putFile(Uri.fromFile(file)).await()
                val url = storageRef.downloadUrl.await().toString()

                val meta = mapOf(
                    "uid" to user.uid,
                    "fileName" to file.name,
                    "type" to type,
                    "size" to file.length(),
                    "url" to url,
                    "timestamp" to System.currentTimeMillis()
                )

                firestore.collection("uploads").add(meta).await()

                downloadUrl = url
                uploadMessage = "Upload successful!"
            } catch (e: Exception) {
                uploadMessage = "Error: ${e.message}"
            } finally {
                isUploading = false
            }
        }
    }

    // =============================================================
    // 🧹 Delete File (remote only)
    // =============================================================
    fun deleteFileRemote(fileUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isUploading = true
                uploadMessage = "Deleting file..."
                val ref = storage.getReferenceFromUrl(fileUrl)
                ref.delete().await()
                uploadMessage = "File deleted successfully."
            } catch (e: Exception) {
                uploadMessage = "Failed to delete: ${e.message}"
            } finally {
                isUploading = false
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
