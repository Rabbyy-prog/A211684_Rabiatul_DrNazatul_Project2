package com.example.a211684_rabiatul_drnazatulaini_project2.data.repository

import com.example.a211684_rabiatul_drnazatulaini_project2.data.network.RetrofitInstance
import com.example.a211684_rabiatul_drnazatulaini_project2.data.network.StationResponse

class StationRepository {

    suspend fun getStations( //use suspend to not freeze the UI while waiting after having internet request
        latitude: Double,
        longitude: Double,
        apiKey: String
    ): List<StationResponse> {

        return RetrofitInstance.api.getStations(
            latitude = latitude,
            longitude = longitude,
            apiKey = apiKey
        )
    }
}