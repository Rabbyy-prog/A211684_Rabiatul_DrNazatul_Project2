package com.example.a211684_rabiatul_drnazatulaini_project2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel.ChargingHistoryViewModel
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.provider.ChargingHistoryViewModelProvider

@Composable
fun ChargingHistoryScreen(
    viewModel: ChargingHistoryViewModel =
        viewModel(
            factory =
                ChargingHistoryViewModelProvider.Factory
        )
) {

    val uiState by viewModel
        .chargingHistoryUiState
        .collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement =
            Arrangement.spacedBy(12.dp)
    ) {

        items(uiState.historyList) { history ->

            Card(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier =
                        Modifier.padding(16.dp)
                ) {

                    Text(
                        text = history.stationName,
                        style =
                            MaterialTheme.typography
                                .titleMedium
                    )

                    Text(
                        text =
                            history.chargerName
                    )

                    Text(
                        text =
                            history.connectorType
                    )

                    Text(
                        text =
                            history.totalPrice
                    )

                    Text(
                        text =
                            "${history.batteryPercent}%"
                    )

                    Text(
                        text =
                            history.chargingDate
                    )
                }
            }
        }
    }
}