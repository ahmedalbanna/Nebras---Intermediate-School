package com.example.data.local

import kotlinx.coroutines.flow.Flow

class StudyTaskRepository(private val dao: StudyTaskDao) {

    val allStudyTasks: Flow<List<StudyTask>> = dao.getAllTasks()

    suspend fun insertTask(task: StudyTask): Long {
        return dao.insertTask(task)
    }

    suspend fun insertTasks(tasks: List<StudyTask>) {
        dao.insertTasks(tasks)
    }

    suspend fun updateTask(task: StudyTask) {
        dao.updateTask(task)
    }

    suspend fun deleteTask(taskId: Long) {
        dao.deleteTask(taskId)
    }

    suspend fun clearAllTasks() {
        dao.deleteAllTasks()
    }
}
