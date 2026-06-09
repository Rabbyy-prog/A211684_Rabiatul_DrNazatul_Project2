package com.example.a211684_rabiatul_drnazatulaini_project2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChargingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.Companion.IGNORE)
    suspend fun insert(history: ChargingHistory)

    @Query("SELECT * FROM charging_history ORDER BY id DESC")
    fun getAllHistory(): Flow<List<ChargingHistory>>
}