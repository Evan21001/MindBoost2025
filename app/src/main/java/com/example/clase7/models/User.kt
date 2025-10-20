package com.example.clase7.models

data class User(
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val availableHours: Int = 0,
    val habits: List<String> = emptyList(),
    val streakCount: Int = 0,
    val lastDailyDate: String = ""
)
