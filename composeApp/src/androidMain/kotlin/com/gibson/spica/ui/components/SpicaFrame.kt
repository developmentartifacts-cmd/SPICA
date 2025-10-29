package com.gibson.spica.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.draw.blur

@Composable
fun SpicaFrame(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(28.dp)
    val blurRadius = with(LocalDensity.current) { 25.dp.toPx() } // 🌀 softness of glass effect

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .clip(shape)
            .graphicsLayer {
                alpha = 0.96f
                shadowElevation = 0f
            }
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)
                    )
                ),
                shape = shape
            )
            .blur(20.dp) // 💫 adds smooth blur background
            .padding(16.dp),
        content = content
    )
}
