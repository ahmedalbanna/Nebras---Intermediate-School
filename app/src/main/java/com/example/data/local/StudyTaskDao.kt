package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyTaskDao {
    @Query("SELECT * FROM study_tasks ORDER BY id ASC")
    fun getAllTasks(): Flow<List<StudyTask>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTask): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<StudyTask>)

    @Update
    suspend fun updateTask(task: StudyTask)

    @Query("DELETE FROM study_tasks WHERE id = :taskId")
    suspend fun deleteTask(taskId: Long)

    @Query("DELETE FROM study_tasks")
    suspend fun deleteAllTasks()
}
