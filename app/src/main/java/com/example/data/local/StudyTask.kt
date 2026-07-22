package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_tasks")
data class StudyTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dayName: String, // مثال: "اليوم 1 (الأحد)", "اليوم 2"
    val subject: String, // المادة الدراسية
    val title: String, // عنوان الوحدة أو الدرس
    val taskType: String, // "STUDY" (وحدة دراسية), "EXERCISE" (تمارين), "QUIZ" (اختبار)
    val durationMinutes: Int, // المدة بالدقائق
    val description: String, // تفاصيل الدرس أو النصحية
    val isCompleted: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
