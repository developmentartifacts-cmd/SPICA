package com.gibson.spica.model

data class PlatformFile(
    val uri: String,
    val name: String,
    val type: String,
    val size: Long,
    val extension: String
)
