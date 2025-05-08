package com.example.clockitproject.scheduleapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

/**
 * ViewModel that holds a list of Tasks and survives configuration changes.
 */
class TaskViewModel : ViewModel() {
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks

    init {
        // sample items for preview / initial state
        _tasks.addAll(
            listOf(
                Task("CSC 430", "College of Staten Island, Room 1N 118", "9:05", "12:05"),
                Task("CSC 330", "College of Staten Island, Room 1N 118", "2:05", "3:15")
            )
        )
    }

    fun addTask(task: Task) {
        _tasks.add(task)
    }
}