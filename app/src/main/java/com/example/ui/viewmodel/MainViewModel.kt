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

import com.example.data.local.StudyTask
import com.example.data.local.StudyTaskRepository
import org.json.JSONArray

sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val geminiRepository = GeminiRepository()
    private val savedRepository: SavedItemRepository
    private val studyTaskRepository: StudyTaskRepository

    init {
        val db = AppDatabase.getDatabase(application)
        savedRepository = SavedItemRepository(db.savedItemDao())
        studyTaskRepository = StudyTaskRepository(db.studyTaskDao())
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

    // State for Study Plan
    private val _studyPlanState = MutableStateFlow<UiState<List<StudyTask>>>(UiState.Idle)
    val studyPlanState: StateFlow<UiState<List<StudyTask>>> = _studyPlanState.asStateFlow()

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

    // Saved Study Tasks from Room DB
    val dbStudyTasks: StateFlow<List<StudyTask>> = studyTaskRepository.allStudyTasks
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

    fun generateStudyPlan(
        subjects: List<String>,
        level: String,
        pace: String,
        dailyHours: String,
        weakTopics: String
    ) {
        if (subjects.isEmpty()) {
            _messageState.value = "يرجى اختيار مادة واحدة على الأقل"
            return
        }
        viewModelScope.launch {
            _studyPlanState.value = UiState.Loading
            val result = geminiRepository.generateStudyPlan(
                subjects = subjects,
                currentLevel = level,
                learningPace = pace,
                dailyHours = dailyHours,
                weakTopics = weakTopics
            )
            result.fold(
                onSuccess = { rawText ->
                    val tasks = parseStudyTasksFromJson(rawText)
                    if (tasks.isNotEmpty()) {
                        _studyPlanState.value = UiState.Success(tasks)
                    } else {
                        // Fallback sample tasks if JSON parsing produced empty list
                        val fallbackList = createFallbackStudyTasks(subjects)
                        _studyPlanState.value = UiState.Success(fallbackList)
                    }
                },
                onFailure = {
                    _studyPlanState.value = UiState.Error(it.localizedMessage ?: "حدث خطأ أثناء بناء الخطة")
                }
            )
        }
    }

    private fun parseStudyTasksFromJson(jsonString: String): List<StudyTask> {
        val list = mutableListOf<StudyTask>()
        try {
            val cleanJson = if (jsonString.contains("```json")) {
                jsonString.substringAfter("```json").substringBefore("```").trim()
            } else if (jsonString.contains("```")) {
                jsonString.substringAfter("```").substringBefore("```").trim()
            } else {
                jsonString.trim()
            }

            val jsonArray = JSONArray(cleanJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val dayName = obj.optString("dayName", "اليوم ${(i / 2) + 1}")
                val subject = obj.optString("subject", "علوم عامة")
                val title = obj.optString("title", "وحدة دراسية جديدة")
                val taskType = obj.optString("taskType", "STUDY").uppercase()
                val durationMinutes = obj.optInt("durationMinutes", 30)
                val description = obj.optString("description", "مراجعة وفهم الأساس العلمية")

                list.add(
                    StudyTask(
                        id = (i + 1).toLong(),
                        dayName = dayName,
                        subject = subject,
                        title = title,
                        taskType = if (taskType in listOf("STUDY", "EXERCISE", "QUIZ")) taskType else "STUDY",
                        durationMinutes = durationMinutes,
                        description = description,
                        isCompleted = false
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    private fun createFallbackStudyTasks(subjects: List<String>): List<StudyTask> {
        val list = mutableListOf<StudyTask>()
        var idCounter = 1L
        subjects.take(4).forEachIndexed { index, subj ->
            val day = "اليوم ${index + 1}"
            list.add(
                StudyTask(
                    id = idCounter++,
                    dayName = day,
                    subject = subj,
                    title = "شرح وحدة $subj الأساسية",
                    taskType = "STUDY",
                    durationMinutes = 30,
                    description = "قراءة القوانين والمفاهيم الأساسية وتدوين النقاط المهمة."
                )
            )
            list.add(
                StudyTask(
                    id = idCounter++,
                    dayName = day,
                    subject = subj,
                    title = "حل تمارين تطبيقية على $subj",
                    taskType = "EXERCISE",
                    durationMinutes = 20,
                    description = "حل 5 مسائل تدريبية وتطبيق خطوات الحل الصحيحة."
                )
            )
            list.add(
                StudyTask(
                    id = idCounter++,
                    dayName = day,
                    subject = subj,
                    title = "اختبار تقييمي لـ $subj",
                    taskType = "QUIZ",
                    durationMinutes = 15,
                    description = "اختبار فهم سريع مكون من 5 أسئلة اختيار من متعدد."
                )
            )
        }
        return list
    }

    fun toggleTaskCompletion(task: StudyTask) {
        val currentState = _studyPlanState.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map {
                if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
            }
            _studyPlanState.value = UiState.Success(updatedList)
        }
        // Also update in Room DB if exists
        viewModelScope.launch {
            studyTaskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun updateStudyTask(updatedTask: StudyTask) {
        val currentState = _studyPlanState.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.map {
                if (it.id == updatedTask.id) updatedTask else it
            }
            _studyPlanState.value = UiState.Success(updatedList)
        }
        viewModelScope.launch {
            studyTaskRepository.updateTask(updatedTask)
            _messageState.value = "تم تعديل المهام بنجاح ✏️"
        }
    }

    fun addCustomStudyTask(task: StudyTask) {
        val currentState = _studyPlanState.value
        val currentList = if (currentState is UiState.Success) currentState.data else emptyList()
        val newId = System.currentTimeMillis()
        val newTask = task.copy(id = newId)
        _studyPlanState.value = UiState.Success(currentList + newTask)

        viewModelScope.launch {
            studyTaskRepository.insertTask(newTask)
            _messageState.value = "تمت إضافة المهام للجدول بنجاح ➕"
        }
    }

    fun deleteStudyTask(taskId: Long) {
        val currentState = _studyPlanState.value
        if (currentState is UiState.Success) {
            val updatedList = currentState.data.filter { it.id != taskId }
            _studyPlanState.value = UiState.Success(updatedList)
        }
        viewModelScope.launch {
            studyTaskRepository.deleteTask(taskId)
            _messageState.value = "تم حذف المهمة من الجدول"
        }
    }

    fun saveCurrentPlanToDatabase(tasks: List<StudyTask>) {
        viewModelScope.launch {
            studyTaskRepository.clearAllTasks()
            studyTaskRepository.insertTasks(tasks)
            _messageState.value = "تم حفظ الخطة الدراسية في أجهزة الهاتف أوفلاين بنجاح 💾"
        }
    }

    fun loadSavedPlanFromDatabase() {
        viewModelScope.launch {
            val tasks = dbStudyTasks.value
            if (tasks.isNotEmpty()) {
                _studyPlanState.value = UiState.Success(tasks)
                _messageState.value = "تم تحميل الخطة الدراسية المحفوظة 📚"
            } else {
                _messageState.value = "لا توجد خطة محفوظة حالياً"
            }
        }
    }
}
