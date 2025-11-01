package com.gibson.spica.media

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

// Shared data model
data class PlatformFile(
    val name: String,
    val mimeType: String,
    val bytes: ByteArray,
    val uri: Uri
)

// 🔹 Helper: Read file info + bytes from URI
fun readPlatformFileFromUri(context: Context, uri: Uri): PlatformFile? {
    val resolver = context.contentResolver

    // File name
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
        while (input.read(data).also { n = it } != -1) buffer.write(data, 0, n)
        buffer.toByteArray()
    }

    return bytes?.let { PlatformFile(name, mimeType, it, uri) }
}

// 🔹 Core picker composable
@Composable
fun AndroidMediaPicker(
    modifier: Modifier = Modifier,
    mimeFilter: String = "*/*",
    onFilePicked: (PlatformFile?) -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) onFilePicked(readPlatformFileFromUri(context, uri))
    }

    Button(
        onClick = { launcher.launch(mimeFilter) },
        modifier = modifier
    ) { Text("Pick file") }
}

// 🔹 Full demo UI
@Composable
fun AndroidMediaPickerDemo() {
    val context = LocalContext.current
    var pickedFile by remember { mutableStateOf<PlatformFile?>(null) }
    var previewBitmap by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }

    Column(Modifier.padding(16.dp)) {
        AndroidMediaPicker(
            onFilePicked = { file ->
                pickedFile = file
                previewBitmap = if (file?.mimeType?.startsWith("image/") == true) {
                    BitmapFactory.decodeByteArray(file.bytes, 0, file.bytes.size)?.asImageBitmap()
                } else null
            }
        )

        Spacer(Modifier.height(16.dp))

        pickedFile?.let { file ->
            Text("Name: ${file.name}")
            Text("Type: ${file.mimeType}")
            Text("Size: ${file.bytes.size / 1024} KB")

            when {
                // 🖼️ Image preview
                previewBitmap != null -> {
                    Spacer(Modifier.height(16.dp))
                    Image(
                        bitmap = previewBitmap!!,
                        contentDescription = "Preview",
                        modifier = Modifier.size(200.dp)
                    )
                }

                // 🎥 Video preview (ExoPlayer)
                file.mimeType.startsWith("video/") -> {
                    Spacer(Modifier.height(16.dp))
                    VideoPreviewPlayer(file.uri)
                }

                // 📄 Document viewer launcher (PDF, etc.)
                file.mimeType == "application/pdf" -> {
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        openDocument(context, file.uri, file.mimeType)
                    }) { Text("Open Document") }
                }
            }
        }
    }
}

// 🔹 Video player preview (ExoPlayer)
@Composable
fun VideoPreviewPlayer(uri: Uri) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    500
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

// 🔹 Open document in external viewer (PDF, text, etc.)
fun openDocument(context: Context, uri: Uri, mimeType: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Open with"))
}
