package com.gibson.spica.data.model

// A simple shared representation of a file, using KMP-safe types
data class KmpFile(
    val name: String,
    val path: String, // Abstract path (e.g., local URI or system path)
    val size: Long,
    val mimeType: String,
    // The actual bytes are passed/used by the platform implementation
)
