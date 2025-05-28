package com.example.myapplication

data class GameHistory(
    val id: Int = 0,
    val word: String,
    val difficulty: String,
    val duration: Long,
    val date: Long = System.currentTimeMillis()
)
