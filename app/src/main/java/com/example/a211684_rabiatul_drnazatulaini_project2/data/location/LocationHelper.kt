package com.example.a211684_rabiatul_drnazatulaini_project2.data.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

//locationHelper = retrieve user's current latitude & longitude

class LocationHelper( //location services belong to android, to access them android needs context
    private val context: Context
) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context) //access phone's location services

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(onLocationReceived: (Double, Double) -> Unit) {

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null) //last location = most recent known location
            .addOnSuccessListener { location -> //if location is found, store it in a variable location & execute the code below
                if (location != null) {
                    onLocationReceived(location.latitude, location.longitude)
                }
            }
        }

}