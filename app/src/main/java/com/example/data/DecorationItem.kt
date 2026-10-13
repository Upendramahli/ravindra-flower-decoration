package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decoration_items")
data class DecorationItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val flowersUsed: String = "",
    val isPopular: Boolean = false,
    val isAvailable: Boolean = true
)
