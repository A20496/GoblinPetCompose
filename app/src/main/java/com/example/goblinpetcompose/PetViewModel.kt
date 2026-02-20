package com.example.goblinpetcompose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PetViewModel(
    private val dao: UnsafeSessionDao
) : ViewModel() {

    private val _mood = MutableStateFlow(PetMood.NEUTRAL)
    val mood: StateFlow<PetMood> = _mood

    private var walkingStartTime: Long? = null

    val sessions = dao.getAllSessions()
    val totalUnsafeTime = dao.getTotalUnsafeTime()
    val sessionCount = dao.getSessionCount()

    fun startUnsafeSession() {
        if (walkingStartTime == null) {
            walkingStartTime = System.currentTimeMillis()
            _mood.value = PetMood.CONCERNED
        }
    }

    fun stopUnsafeSession() {
        walkingStartTime?.let { start ->
            val end = System.currentTimeMillis()
            val duration = end - start

            viewModelScope.launch {
                dao.insert(
                    UnsafeSession(
                        startTime = start,
                        endTime = end,
                        duration = duration
                    )
                )
            }

            walkingStartTime = null
            _mood.value = PetMood.NEUTRAL
        }
    }

    fun simulateWalking() {
        startUnsafeSession()
    }

    fun simulateStop() {
        stopUnsafeSession()
    }
}