package com.gibson.spica.data.util

// This provides the expected functions for platform-specific file operations.
expect class PlatformFilePicker {
    // 💡 Example: Pick a file, returning the shared KmpFile model
    suspend fun pickFile(mimeTypes: List<String>): KmpFile?
    
    // 💡 Example: Get the raw bytes for uploading (since we can't share platform-specific Streams/URIs)
    suspend fun getFileBytes(kmpFile: KmpFile): ByteArray
    
    // Add functions for saving, deleting, or image specific tasks here
}
