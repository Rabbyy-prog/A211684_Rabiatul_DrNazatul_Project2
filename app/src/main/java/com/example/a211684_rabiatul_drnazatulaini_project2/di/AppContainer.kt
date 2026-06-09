package com.example.a211684_rabiatul_drnazatulaini_project2.di

import android.content.Context
import com.example.a211684_rabiatul_drnazatulaini_project2.data.local.ElecTraxDatabase
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.ChargingHistoryRepository
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.OfflineChargingHistoryRepository

interface AppContainer {

    val chargingHistoryRepository: ChargingHistoryRepository
}

class AppDataContainer(
    private val context: Context
) : AppContainer {

    override val chargingHistoryRepository: ChargingHistoryRepository by lazy {
        OfflineChargingHistoryRepository(
            ElecTraxDatabase.getDatabase(context)
                .chargingHistoryDao()
        )
    }
}