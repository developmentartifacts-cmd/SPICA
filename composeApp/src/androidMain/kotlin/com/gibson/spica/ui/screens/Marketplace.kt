package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarketplaceScreen() {
    val spheres = remember { demoSpheres }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        // Top bar title
        Text(
            text = "SPHERES",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        // Search/Filter stub
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search worlds or industries...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.height(16.dp))

        // List of worlds (Spheres)
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(spheres) { sphere ->
                SphereCard(sphere)
            }
        }

        // Create Sphere button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { /* TODO: Navigate to Sphere creation */ },
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Create Sphere")
            }
        }
    }
}

@Composable
fun SphereCard(sphere: DemoSphere) {
    Surface(
        color = Color(0xFF121212),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Navigate into Sphere details */ }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = sphere.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = sphere.description,
                color = Color.LightGray,
                fontSize = 13.sp
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "${sphere.members} members • ${sphere.category}",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}

// --- Mock data ---
data class DemoSphere(
    val name: String,
    val description: String,
    val members: Int,
    val category: String
)

private val demoSpheres = listOf(
    DemoSphere("OpenScience", "Where researchers and thinkers share discoveries.", 4821, "Science"),
    DemoSphere("AfroFuture", "A digital renaissance of African creators and innovators.", 3021, "Culture"),
    DemoSphere("EcoGenesis", "Reimagining sustainability and green tech.", 2708, "Environment"),
    DemoSphere("MetaCrafters", "Builders and designers shaping the virtual worlds.", 1198, "Technology")
)
