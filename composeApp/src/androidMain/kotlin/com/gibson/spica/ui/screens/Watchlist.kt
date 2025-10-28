package com.gibson.spica.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun WatchlistScreen() {
    val orbitItems = remember {
        listOf(
            "SPICA" to "Sphere",
            "Gibson Ezeh" to "Identity",
            "Creative Africa" to "Sphere",
            "Luna K." to "Identity",
            "NeuroType" to "Project",
            "EchoLens" to "Project"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Your Orbit",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Everything you follow — visualized as your personal universe.",
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            OrbitMap(orbitItems)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = Color(0xFF121212),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Recent Signals", color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                listOf(
                    "Gibson Ezeh posted a new Echo in SPICA Sphere",
                    "NeuroType launched beta collaboration access",
                    "Luna K. followed you",
                    "Creative Africa Sphere trending"
                ).forEach {
                    Text(it, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }
    }
}

@Composable
fun OrbitMap(items: List<Pair<String, String>>) {
    val orbitColors = listOf(
        Color.White,
        Color.Gray,
        Color.LightGray
    )

    Canvas(modifier = Modifier.size(280.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radiusStep = 35.dp.toPx()
        val itemCount = items.size

        drawIntoCanvas {
            // Draw concentric orbit rings
            for (i in 1..3) {
                drawCircle(
                    color = Color.DarkGray.copy(alpha = 0.4f),
                    radius = i * radiusStep,
                    center = center,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.dp.toPx())
                )
            }

            // Draw planets
            items.forEachIndexed { index, (name, type) ->
                val orbitLevel = (index % 3) + 1
                val angle = (index * (360f / itemCount)) * (Math.PI / 180f)
                val planetX = center.x + cos(angle).toFloat() * (orbitLevel * radiusStep)
                val planetY = center.y + sin(angle).toFloat() * (orbitLevel * radiusStep)

                drawCircle(
                    color = orbitColors[index % orbitColors.size],
                    radius = 10.dp.toPx(),
                    center = Offset(planetX, planetY)
                )
            }
        }
    }
}
