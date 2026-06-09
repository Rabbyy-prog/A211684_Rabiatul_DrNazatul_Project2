package com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a211684_rabiatul_drnazatulaini_project2.data.local.ChargingHistory
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.ChargingHistoryRepository
import com.example.a211684_rabiatul_drnazatulaini_project2.model.ChargerUiState
import com.example.a211684_rabiatul_drnazatulaini_project2.model.ChargingUiState
import com.example.a211684_rabiatul_drnazatulaini_project2.model.Connector
import com.example.a211684_rabiatul_drnazatulaini_project2.model.StationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ChargingViewModel(
    private val chargingHistoryRepository: ChargingHistoryRepository
) : ViewModel() {
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
        val battery = Random.Default.nextInt(20, 91)
        val usage =
            when{
                battery <40 ->
                    Random.Default.nextInt(80,121)

                battery < 70 ->
                    Random.Default.nextInt(50, 90)

                else ->
                    Random.Default.nextInt(20, 50)
            }
        val time =
            when{
                battery <40 ->
                    Random.Default.nextInt(25, 46)
                battery <70 ->
                    Random.Default.nextInt(15,35)
                else ->
                    Random.Default.nextInt(5,20)
            }
        val energyNeeded =
            (100 - battery) * 0.8

        val total =
            energyNeeded * 0.30

        val price =
            String.format("RM %.2f", total)


        _uiState.update {
            it.copy(
                batteryPercent = battery,
                currentUsage = "$usage kW",
                timeRemaining = "$time min",
                totalPrice = price
            )

        }
    }
    fun saveChargingHistory() {

        viewModelScope.launch {

            val currentState = uiState.value

            val currentDate =
                SimpleDateFormat(
                    "dd MMM yyyy, hh:mm a",
                    Locale.getDefault()
                ).format(Date())

            chargingHistoryRepository.insertHistory(
                ChargingHistory(
                    stationName =
                        currentState.selectedStation?.stationName ?: "",

                    chargerName =
                        currentState.selectedCharger?.chargerName ?: "",

                    connectorType =
                        currentState.selectedConnector?.type ?: "",

                    batteryPercent =
                        currentState.batteryPercent,

                    totalPrice =
                        currentState.totalPrice,

                    chargingDate =
                        currentDate
                )
            )
        }
    }
}