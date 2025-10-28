package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
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
fun HomeScreen() {
    val posts = remember { demoPosts }   // Mocked stream items

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        // Top Bar
        Text(
            text = "SPICA STREAM",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        // Echo Composer (Post input stub)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Share something with the world...", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(Color.Transparent),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.DarkGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(Modifier.height(16.dp))

        // Feed List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(posts) { post ->
                EchoCard(post)
            }
        }

        // Floating Action Button for New Echo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            FloatingActionButton(
                onClick = { /* TODO: open composer dialog */ },
                containerColor = Color.White,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Post")
            }
        }
    }
}

@Composable
fun EchoCard(post: DemoPost) {
    Surface(
        color = Color(0xFF121212),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.author,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = post.content,
                color = Color.LightGray,
                fontSize = 14.sp
            )
        }
    }
}

// --- Mock data until backend connects ---
data class DemoPost(val author: String, val content: String)

private val demoPosts = listOf(
    DemoPost("Ava", "Humanity is building the next layer of consciousness."),
    DemoPost("Kofi", "SPICA feels like a universe, not an app."),
    DemoPost("Liam", "Every post is a signal. Every person is a world."),
    DemoPost("Aisha", "Art, code, philosophy — all merge here.")
)
