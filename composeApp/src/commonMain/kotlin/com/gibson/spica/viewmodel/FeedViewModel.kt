package com.gibson.spica.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.gibson.spica.model.Post

class FeedViewModel : ViewModel() {

    val posts = mutableStateListOf<Post>()

    init {
        loadMockPosts()
    }

    private fun loadMockPosts() {
        posts.clear()
        val categories = listOf("Stream", "Sphere", "Identity", "Orbit")

        repeat(12) { i ->
            val category = categories[i % categories.size]
            posts += Post(
                id = "post_$i",
                authorName = "User $i",
                text = "This is a SPICA $category post — the universal expression layer.",
                category = category
            )
        }
    }

    fun getPostsByCategory(category: String): List<Post> =
        posts.filter { it.category == category }
}
