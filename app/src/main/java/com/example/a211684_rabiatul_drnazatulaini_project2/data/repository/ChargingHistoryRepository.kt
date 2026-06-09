package com.example.a211684_rabiatul_drnazatulaini_project2.data.repository

import com.example.a211684_rabiatul_drnazatulaini_project2.data.local.ChargingHistory
import com.example.a211684_rabiatul_drnazatulaini_project2.data.local.ChargingHistoryDao
import kotlinx.coroutines.flow.Flow

interface ChargingHistoryRepository {

    fun getAllHistoryStream(): Flow<List<ChargingHistory>>

    suspend fun insertHistory(history: ChargingHistory)
}

class OfflineChargingHistoryRepository(
    private val chargingHistoryDao: ChargingHistoryDao
) : ChargingHistoryRepository {

    override fun getAllHistoryStream(): Flow<List<ChargingHistory>> =
        chargingHistoryDao.getAllHistory()

    override suspend fun insertHistory(history: ChargingHistory) =
        chargingHistoryDao.insert(history)
}