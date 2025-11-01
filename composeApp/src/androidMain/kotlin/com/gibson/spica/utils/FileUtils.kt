package com.gibson.spica.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import com.gibson.spica.model.PlatformFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

/**
 * Reads a file from a URI into PlatformFile (name, mime, bytes).
 */
fun readPlatformFileFromUri(context: Context, uri: Uri): PlatformFile? {
    val resolver = context.contentResolver

    var name = "file"
    resolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) {
            name = cursor.getString(nameIndex) ?: name
        }
    }

    val mime = resolver.getType(uri) ?: "application/octet-stream"

    val bytes = try {
        resolver.openInputStream(uri)?.use { input ->
            val buffer = ByteArrayOutputStream()
            val data = ByteArray(8 * 1024)
            var read: Int
            while (input.read(data).also { read = it } != -1) {
                buffer.write(data, 0, read)
            }
            buffer.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    return bytes?.let { PlatformFile(name, mime, it) }
}

/**
 * Write a PlatformFile temporarily for sharing or playback.
 */
fun writeTempFile(context: Context, file: PlatformFile): File {
    val safeName = file.name.replace(Regex("[^A-Za-z0-9_.-]"), "_")
    val tempFile = File.createTempFile("temp_", "_$safeName", context.cacheDir)
    FileOutputStream(tempFile).use { fos ->
        fos.write(file.bytes)
        fos.flush()
    }
    return tempFile
}

/**
 * Opens the given file with an external system app.
 */
fun openWithExternalApp(context: Context, file: PlatformFile) {
    val tempFile = writeTempFile(context, file)
    val authority = "${context.packageName}.fileprovider"
    val uri = FileProvider.getUriForFile(context, authority, tempFile)

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, file.mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(intent, "Open with"))
}
