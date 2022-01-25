package com.example.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photos(
    val albumId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)