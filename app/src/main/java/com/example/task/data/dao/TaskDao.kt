package com.example.task.data.dao

import androidx.room.*
import com.example.task.model.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM task ORDER By id DESC LIMIT 30")
    suspend fun taskListNotLiveData(): List<Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(task: Task)

    @Delete
    fun delete(task: Task)
}