package com.example.task.ui.tasklist

import com.example.task.model.Task

interface TaskClickCallback {
    fun onClick(task: Task)
}