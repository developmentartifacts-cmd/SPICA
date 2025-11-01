package com.gibson.spica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gibson.spica.model.PlatformFile
import com.gibson.spica.util.FileUtils
import com.gibson.spica.viewmodel.MediaControllerViewModel

@Composable
fun MediaControllerView(
    viewModel: MediaControllerViewModel,
    onPickFile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val file by viewModel.selectedFile

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF121212), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        if (file == null) {
            Text(
                text = "Tap to select media or file",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .clickable { onPickFile() }
            )
        } else {
            MediaPreview(file = file!!, onRemove = { viewModel.clearSelection() })
        }
    }
}

@Composable
private fun MediaPreview(file: PlatformFile, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        when {
            FileUtils.isImage(file) -> {
                AsyncImage(
                    model = file.uri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            FileUtils.isVideo(file) -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Video",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(text = file.name, color = Color.White)
                }
            }
            FileUtils.isAudio(file) -> {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🎵 ${file.name}", color = Color.White)
                }
            }
            else -> {
                Row(
                    Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "File",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = file.name, color = Color.White)
                }
            }
        }

        IconButton(
            onClick = onRemove,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = Color.White
            )
        }
    }
}
