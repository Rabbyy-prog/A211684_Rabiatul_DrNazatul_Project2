package com.example.a211684_rabiatul_drnazatulaini_lab4.data

data class ChargerUiState (
            val chargerName: String,
            val power: String,
            val connectors: List<Connector>
)
        data class Connector(
            val number: Int,
            val type: String,
            val isAvailable: Boolean
        )
