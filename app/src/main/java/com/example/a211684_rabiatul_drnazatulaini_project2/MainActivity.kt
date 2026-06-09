package com.example.a211684_rabiatul_drnazatulaini_project2

import android.os.Bundle
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.a211684_rabiatul_drnazatulaini_project1.ui.theme.A211684_Rabiatul_DrNazatulAini_Project1Theme
import com.example.a211684_rabiatul_drnazatulaini_project2.data.location.LocationHelper
import com.example.a211684_rabiatul_drnazatulaini_project2.navigation.ElecTraxApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //make UI go full screen
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }

        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val locationHelper = LocationHelper(this)

            locationHelper.getCurrentLocation { latitude, longitude ->

                Log.d(
                    "LOCATION_TEST",
                    "Latitude: $latitude Longitude: $longitude"
                )
            }

            setContent {
                A211684_Rabiatul_DrNazatulAini_Project1Theme() {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {

                        ElecTraxApp()
                    }
                }
            }
        }
    }
}
