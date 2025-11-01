package com.gibson.spica.media

/**
 * Platform-independent file model.
 * Used to represent a selected file (name, MIME type, bytes).
 */
data class PlatformFile(
    val name: String,
    val mimeType: String,
    val bytes: ByteArray
)
