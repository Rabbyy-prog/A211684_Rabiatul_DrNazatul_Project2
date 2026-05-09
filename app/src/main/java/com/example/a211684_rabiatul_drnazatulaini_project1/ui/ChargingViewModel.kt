package com.example.a211684_rabiatul_drnazatulaini_project1.ui

import androidx.lifecycle.ViewModel
import com.example.a211684_rabiatul_drnazatulaini_project1.data.StationUiState
import com.example.a211684_rabiatul_drnazatulaini_project1.data.ChargerUiState
import com.example.a211684_rabiatul_drnazatulaini_project1.data.ChargingUiState
import com.example.a211684_rabiatul_drnazatulaini_project1.data.Connector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random
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

    fun generateChargingSession(){
        val battery = Random.nextInt(20, 91)
        val usage =
            when{
                battery <40 ->
                    Random.nextInt(80,121)

                battery < 70 ->
                    Random.nextInt(50, 90)

                else ->
                    Random.nextInt(20, 50)
            }
        val time =
            when{
                battery <40 ->
                    Random.nextInt(25, 46)
                battery <70 ->
                    Random.nextInt(15,35)
                else ->
                    Random.nextInt(5,20)
            }
        val energyNeeded =
            (100 - battery) * 0.8

        val total =
            energyNeeded * 0.60

        val price =
            String.format(
                "RM %.2f",
                total
            )


        _uiState.update {
            it.copy(
                batteryPercent = battery,
                currentUsage = "$usage kW",
                timeRemaining = "$time min",
                totalPrice = price
            )

        }
    }
}
