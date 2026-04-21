package com.example.a211684_rabiatul_drnazatulaini_lab4.ui

import androidx.lifecycle.ViewModel
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.ChargerUiState
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.ChargingUiState
import com.example.a211684_rabiatul_drnazatulaini_lab4.data.Connector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChargingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChargingUiState())
    val uiState: StateFlow<ChargingUiState> = _uiState.asStateFlow()

    fun updateStation(selectedStation: StationUiState) {
        _uiState.update { it.copy(selectedStation = selectedStation) }
    }

    fun updateConnector(connector: Connector) {
        _uiState.update { it.copy(selectedConnector = connector) }
    }
    fun updateCharger(selectedCharger: ChargerUiState) {
        _uiState.update { it.copy(selectedCharger = selectedCharger) }
    }

    fun resetSession() {
        _uiState.value = ChargingUiState()
    }
}
