package com.gibson.spica.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gibson.spica.ui.theme.SpicaTheme

@Composable
fun PortfolioScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Profile header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable { /* TODO: edit profile pic */ }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Username", fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
                    Text("Bio or tagline goes here", fontSize = 14.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        item {
            // “About” Section
            SectionCard(title = "About") {
                Text(
                    "This is your personal canvas. Share who you are, your interests, and your identity within SPICA.",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        item {
            // “My Work / Creations” Section
            SectionCard(title = "My Work") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(3) { index ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clickable { /* TODO: open creation */ },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text("Creation #${index + 1}", color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }
        }

        item {
            // “Connections / Collaborations” Section
            SectionCard(title = "Connections") {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    repeat(5) { idx ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondary)
                                .clickable { /* TODO: view connection */ }
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            content()
        }
    }
}
