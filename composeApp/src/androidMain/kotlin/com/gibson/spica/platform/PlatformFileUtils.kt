// androidMain/src/.../PlatformFileUtils.kt
package com.gibson.spica.platform

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream

fun readPlatformFileFromUri(context: Context, uri: Uri): PlatformFile? {
    val resolver = context.contentResolver

    // Get filename
    var name = "file"
    resolver.query(uri, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && cursor.moveToFirst()) {
            name = cursor.getString(nameIndex)
        }
    }

    val mimeType = resolver.getType(uri) ?: "application/octet-stream"

    val bytes = resolver.openInputStream(uri)?.use { input ->
        val buffer = ByteArrayOutputStream()
        val data = ByteArray(8192)
        var n: Int
        while (true) {
            n = input.read(data)
            if (n <= 0) break
            buffer.write(data, 0, n)
        }
        buffer.toByteArray()
    } ?: return null

    return PlatformFile(name = name, mimeType = mimeType, bytes = bytes)
}
