package com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a211684_rabiatul_drnazatulaini_project2.data.repository.StationRepository
import com.example.a211684_rabiatul_drnazatulaini_project2.model.StationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StationViewModel : ViewModel() {

    private val repository = StationRepository()

    private val _stations =
        MutableStateFlow<List<StationUiState>>(emptyList())

    val stations: StateFlow<List<StationUiState>> =
        _stations

     fun loadStations(
         latitude: Double,
         longitude: Double
     ) {
        viewModelScope.launch {

    try {
        val stations = repository.getStations( //get data from the API
            latitude = latitude, //pass GPS coordinates to OpenChargeMap
            longitude = longitude,
            apiKey = "a613e783-6c83-450a-9348-96566ec90957"
        )

        stations.forEach { station ->
            println("Station Name: ${station.AddressInfo.Title}")
            println("Distance: ${station.AddressInfo.Distance}")
        }
        _stations.value = stations.map { station ->

            StationUiState(
                stationName = station.AddressInfo.Title,
                power = "${station.Connections.firstOrNull()?.PowerKW ?: 0} kW",
                distance = String.format("%.1f km", station.AddressInfo.Distance ?: 0.0),
                rating = "4.5",
                dc = "DC",
                ac = "0"
            )
        }
    } catch (e: Exception){
        e.printStackTrace()
    }

            }
        }
    }