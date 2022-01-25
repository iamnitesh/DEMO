package com.example.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Posts(
    val body: String,
    val email: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val postId: Int
)