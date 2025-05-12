package com.example.clockitproject.scheduleapp

import java.time.LocalDate

data class Task(
    val title: String,
    val location: String,
    val startTime: String,
    val endTime: String,
    val date: LocalDate
)