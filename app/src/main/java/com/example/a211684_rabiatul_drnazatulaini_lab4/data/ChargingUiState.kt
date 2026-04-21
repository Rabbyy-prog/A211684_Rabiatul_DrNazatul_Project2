package com.example.a211684_rabiatul_drnazatulaini_lab4.data

data class ChargingUiState(
    val selectedStation: StationUiState? = null,
    val selectedCharger: ChargerUiState? = null,
    val selectedPort: Int? = null,
    val selectedConnector: Connector? = null

)