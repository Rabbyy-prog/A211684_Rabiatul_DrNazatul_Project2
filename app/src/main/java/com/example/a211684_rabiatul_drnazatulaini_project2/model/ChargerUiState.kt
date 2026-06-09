package com.example.a211684_rabiatul_drnazatulaini_project2.model
 //just charger data, what chargers exist
data class ChargerUiState (
            val chargerName: String,
            val power: String,
            val connectors: List<Connector>

)
        data class Connector(
            val number: Int,
            val type: String,
            var isAvailable: Boolean,
            var isCurrentUser: Boolean = false
        )
