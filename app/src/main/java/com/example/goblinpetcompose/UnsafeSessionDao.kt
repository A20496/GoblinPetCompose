package com.example.goblinpetcompose

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UnsafeSessionDao {

    @Insert
    suspend fun insert(session: UnsafeSession)

    @Query("SELECT * FROM UnsafeSession")
    fun getAllSessions(): Flow<List<UnsafeSession>>

    @Query("SELECT SUM(duration) FROM UnsafeSession")
    fun getTotalUnsafeTime(): Flow<Long?>

    @Query("SELECT COUNT(*) FROM UnsafeSession")
    fun getSessionCount(): Flow<Int>
}