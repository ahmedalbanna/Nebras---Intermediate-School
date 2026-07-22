package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_items")
data class SavedItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val subject: String,
    val itemType: String, // CONCEPT, MATH_SOLVER, SUMMARY, ESSAY, QUIZ
    val inputContent: String,
    val outputContent: String,
    val timestamp: Long = System.currentTimeMillis()
)
