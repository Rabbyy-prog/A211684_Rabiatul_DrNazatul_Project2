package com.example.a211684_rabiatul_drnazatulaini_project2.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import java.util.Locale

//describe the REQUEST that we SEND

interface StationApiService{

    @GET("poi/") //to get the api baseUrl (resource), baseUrl + @GET
    suspend fun getStations( //use suspend to allow function to pause while waiting for response as APi may take time to complete requests
        @Query("output") output: String = "json", //data is send in JSON format
        @Query("latitude") latitude: Double, //how far north or south (for GPS)
        @Query("longitude") longitude: Double, //how far east or west (for GPS)
        @Query("distance") distance: Int = 20,
        @Query("distanceunit") distanceUnit: String = "KM",
        @Query("maxresults") maxResults: Int = 10,
        @Query("key") apiKey: String
    ): List<StationResponse>
}