package com.example.inventory.data

// User.kt


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey val username: String,
    val passwordHash: String
)
