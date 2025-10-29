package com.gibson.spica.model

data class Post(
    val id: String = "",
    val authorName: String = "",
    val authorAvatarUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val text: String = "",
    val imageUrl: String? = null,
    val category: String = "" // Stream, Sphere, Identity, Orbit
)
