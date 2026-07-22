package com.example.data.model

data class QuizQuestion(
    val id: Int = 0,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

data class InteractiveQuiz(
    val title: String,
    val subject: String,
    val questions: List<QuizQuestion>
)
