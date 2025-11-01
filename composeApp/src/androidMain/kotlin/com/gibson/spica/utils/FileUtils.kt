package com.gibson.spica.util

import com.gibson.spica.model.PlatformFile

object FileUtils {

    fun isImage(file: PlatformFile) = file.type.startsWith("image")
    fun isVideo(file: PlatformFile) = file.type.startsWith("video")
    fun isAudio(file: PlatformFile) = file.type.startsWith("audio")
    fun isDocument(file: PlatformFile) =
        listOf("application/pdf", "application/msword", "text/plain").contains(file.type)

    fun readableFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return String.format("%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
    }
}
