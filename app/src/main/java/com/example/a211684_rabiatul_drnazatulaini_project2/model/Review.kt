package com.example.a211684_rabiatul_drnazatulaini_project2.model

data class Review(
    val stationName: String = "",
    val chargerName: String = "",
    val connectorNumber: Int = 0,
    val chargingPort: String = "",
    val rating: Int = 0,
    val review: String = "",
    val dateTime: String = ""
)

data class StationUiState(
    val id: String = "ELECTRAX CHARGER",
    val stationName: String,
    val power: String,
    val distance: String,
    val rating: String,
    val dc: String,
    val ac: String,
    val address: String = "Persiaran Kemajuan, Seksyen 1, Bangi",
    val operator: String = "EVC_ElecTrax"
)