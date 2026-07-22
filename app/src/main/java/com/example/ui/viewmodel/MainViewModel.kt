package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.api.GeminiRepository
import com.example.data.local.AppDatabase
import com.example.data.local.SavedItem
import com.example.data.local.SavedItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiRepository = GeminiRepository()
    private val savedRepository: SavedItemRepository

    init {
        val dao = AppDatabase.getDatabase(application).savedItemDao()
        savedRepository = SavedItemRepository(dao)
    }

    // State for Concept Explanation
    private val _conceptState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val conceptState: StateFlow<UiState<String>> = _conceptState.asStateFlow()

    // State for Math/Physics Solver
    private val _mathSolverState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val mathSolverState: StateFlow<UiState<String>> = _mathSolverState.asStateFlow()

    // State for Similar Practice Problem
    private val _practiceProblemState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val practiceProblemState: StateFlow<UiState<String>> = _practiceProblemState.asStateFlow()

    // State for Lesson Summary
    private val _summaryState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val summaryState: StateFlow<UiState<String>> = _summaryState.asStateFlow()

    // State for Essay Corrector
    private val _essayState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val essayState: StateFlow<UiState<String>> = _essayState.asStateFlow()

    // State for Quiz Generator
    private val _quizState = MutableStateFlow<UiState<String>>(UiState.Idle)
    val quizState: StateFlow<UiState<String>> = _quizState.asStateFlow()

    // Toast / Message State
    private val _messageState = MutableStateFlow<String?>(null)
    val messageState: StateFlow<String?> = _messageState.asStateFlow()

    // All Saved Items from Room DB
    val savedItems: StateFlow<List<SavedItem>> = savedRepository.allSavedItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun clearMessage() {
        _messageState.value = null
    }

    fun explainConcept(topic: String, subject: String, level: String = "عادي") {
        if (topic.isBlank()) return
        viewModelScope.launch {
            _conceptState.value = UiState.Loading
            val result = geminiRepository.explainConcept(topic, subject, level)
            result.fold(
                onSuccess = { _conceptState.value = UiState.Success(it) },
                onFailure = { _conceptState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ غير معروف") }
            )
        }
    }

    fun solveMathPhysics(problemText: String, subject: String) {
        if (problemText.isBlank()) return
        viewModelScope.launch {
            _mathSolverState.value = UiState.Loading
            _practiceProblemState.value = UiState.Idle
            val result = geminiRepository.solveMathPhysics(problemText, subject)
            result.fold(
                onSuccess = { _mathSolverState.value = UiState.Success(it) },
                onFailure = { _mathSolverState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ") }
            )
        }
    }

    fun generatePracticeProblem(originalProblem: String, subject: String) {
        if (originalProblem.isBlank()) return
        viewModelScope.launch {
            _practiceProblemState.value = UiState.Loading
            val result = geminiRepository.generateSimilarProblem(originalProblem, subject)
            result.fold(
                onSuccess = { _practiceProblemState.value = UiState.Success(it) },
                onFailure = { _practiceProblemState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ") }
            )
        }
    }

    fun summarizeLesson(lessonContent: String, subject: String) {
        if (lessonContent.isBlank()) return
        viewModelScope.launch {
            _summaryState.value = UiState.Loading
            val result = geminiRepository.summarizeLesson(lessonContent, subject)
            result.fold(
                onSuccess = { _summaryState.value = UiState.Success(it) },
                onFailure = { _summaryState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ") }
            )
        }
    }

    fun correctEssay(essayText: String, topic: String) {
        if (essayText.isBlank()) return
        viewModelScope.launch {
            _essayState.value = UiState.Loading
            val result = geminiRepository.correctEssay(essayText, topic)
            result.fold(
                onSuccess = { _essayState.value = UiState.Success(it) },
                onFailure = { _essayState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ") }
            )
        }
    }

    fun generateQuiz(topic: String, subject: String) {
        if (topic.isBlank()) return
        viewModelScope.launch {
            _quizState.value = UiState.Loading
            val result = geminiRepository.generateQuiz(topic, subject)
            result.fold(
                onSuccess = { _quizState.value = UiState.Success(it) },
                onFailure = { _quizState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ") }
            )
        }
    }

    fun saveResultToLibrary(title: String, subject: String, itemType: String, inputContent: String, outputContent: String) {
        viewModelScope.launch {
            val item = SavedItem(
                title = title.ifBlank { "عنصر محفوط" },
                subject = subject,
                itemType = itemType,
                inputContent = inputContent,
                outputContent = outputContent
            )
            savedRepository.saveItem(item)
            _messageState.value = "تم الحفظ في المكتبة بنجاح ✅"
        }
    }

    fun deleteSavedItem(id: Long) {
        viewModelScope.launch {
            savedRepository.deleteItem(id)
            _messageState.value = "تم حذف العنصر من المكتبة"
        }
    }
}
