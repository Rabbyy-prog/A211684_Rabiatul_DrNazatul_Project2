package com.example.a211684_rabiatul_drnazatulaini_project1.data


//this ui contain what data exists, data type and default value
data class ChargingUiState(
    val selectedStation: StationUiState? = null,
    val selectedCharger: ChargerUiState? = null,
    val selectedPort: Int? = null,
    val selectedConnector: Connector? = null,
    val batteryPercent: Int = 0,
    val currentUsage: String = "",
    val timeRemaining: String = "",
    val totalPrice: String = ""



)