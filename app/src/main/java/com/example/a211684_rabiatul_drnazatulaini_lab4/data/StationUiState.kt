package com.example.a211684_rabiatul_drnazatulaini_lab4.data

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
