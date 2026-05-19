package com.example.a211684_rabiatul_drnazatulaini_lab5.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.ChargingHistory
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.ChargingHistoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val TIMEOUT_MILLIS = 5_000L

data class ChargingHistoryUiState(
    val historyList: List<ChargingHistory> = listOf()
)

class ChargingHistoryViewModel(
    chargingHistoryRepository: ChargingHistoryRepository
) : ViewModel() {

    val chargingHistoryUiState:
            StateFlow<ChargingHistoryUiState> =

        chargingHistoryRepository
            .getAllHistoryStream()
            .map {
                ChargingHistoryUiState(it)
            }
            .stateIn(
                scope = viewModelScope,
                started =
                    SharingStarted.WhileSubscribed(
                        TIMEOUT_MILLIS
                    ),
                initialValue =
                    ChargingHistoryUiState()
            )
}