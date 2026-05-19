package com.example.a211684_rabiatul_drnazatulaini_lab5.data

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