package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PortfolioScreen() {
    var selectedTab by remember { mutableStateOf("Posts") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // 🪞 Profile Header
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray)
            )
            Spacer(Modifier.height(12.dp))
            Text("Gibson Ezeh", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("@gibson", color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Creator • Thinker • Builder",
                color = Color.LightGray,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                StatItem("Followers", "2.3K")
                StatItem("Following", "120")
                StatItem("Spheres", "8")
            }
        }

        Spacer(Modifier.height(20.dp))

        // 🔖 Tabs: Posts / Creations / Connections
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Posts", "Creations", "Connections").forEach { tab ->
                TextButton(onClick = { selectedTab = tab }) {
                    Text(
                        text = tab,
                        color = if (selectedTab == tab) Color.White else Color.Gray,
                        fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // 🧩 Tab content
        when (selectedTab) {
            "Posts" -> UserPosts()
            "Creations" -> UserCreations()
            "Connections" -> UserConnections()
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun UserPosts() {
    val posts = listOf(
        "The future of creation belongs to collaboration.",
        "I believe in building tools that amplify human potential.",
        "SPICA is a new layer of the internet — for ideas, not just information."
    )
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(posts) { post ->
            Surface(
                color = Color(0xFF121212),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = post,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun UserCreations() {
    val creations = listOf("EchoLens", "NeuroType", "GreenSpark", "SPICA")
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(creations) { creation ->
            Surface(
                color = Color(0xFF121212),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(creation, color = Color.White, fontWeight = FontWeight.Bold)
                    Text("Project or collaboration", color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun UserConnections() {
    val connections = listOf("Amaka Obi", "John Kintu", "Leila Abebe", "Chris Mensah")
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(connections) { name ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.DarkGray, shape = MaterialTheme.shapes.small)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(name, color = Color.White)
                TextButton(onClick = { /* TODO: navigate to user's portfolio */ }) {
                    Text("View", color = Color.Gray)
                }
            }
        }
    }
}
