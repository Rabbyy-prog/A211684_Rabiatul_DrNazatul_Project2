package com.example.a211684_rabiatul_drnazatulaini_lab5.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "charging_history")
data class ChargingHistory(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val stationName: String,
    val chargerName: String,
    val connectorType: String,
    val batteryPercent: Int,
    val totalPrice: String,
    val chargingDate: String
)