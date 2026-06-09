package com.example.a211684_rabiatul_drnazatulaini_project2.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance { //use object as only ONE instance exists in the entire app

    private const val BASE_URL = "https://api.openchargemap.io/v3/" //private only in this file onlyy

    val api: StationApiService by lazy { //use lazy to only initialize when needed
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //whenever receiving json, use Gson to convert it into data classes
            .build()
            .create(StationApiService::class.java) //tell retrofit to generate the implementation of stationApiServicei= interface so that API functions such as getStations can be executed.
    }
}