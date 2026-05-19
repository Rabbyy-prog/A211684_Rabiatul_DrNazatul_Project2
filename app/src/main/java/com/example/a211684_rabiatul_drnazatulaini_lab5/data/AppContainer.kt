package com.example.a211684_rabiatul_drnazatulaini_lab5.data

import android.content.Context

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