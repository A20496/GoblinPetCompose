package com.example.goblinpetcompose

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UnsafeSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val startTime: Long,
    val endTime: Long,
    val duration: Long
)