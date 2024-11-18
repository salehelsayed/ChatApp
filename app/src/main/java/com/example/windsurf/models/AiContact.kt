package com.example.windsurf.models

data class AiContact(
    val id: Int,
    val name: String,
    val role: String,
    val systemPrompt: String,
    val avatarResId: Int
)
