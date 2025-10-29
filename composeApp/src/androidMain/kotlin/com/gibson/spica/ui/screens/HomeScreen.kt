package com.gibson.spica.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import com.gibson.spica.ui.components.SpicaFrame
import com.gibson.spica.ui.components.PostCard
import com.gibson.spica.viewmodel.FeedViewModel

@Composable
fun HomeScreen(viewModel: FeedViewModel = remember { FeedViewModel() }) {
    SpicaFrame {
        LazyColumn {
            items(viewModel.getPostsByCategory("Stream")) { post ->
                PostCard(post)
            }
        }
    }
}
