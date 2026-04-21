package com.example.a211684_rabiatul_drnazatulaini_lab4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.a211684_rabiatul_drnazatulaini_lab4.ui.theme.A211684_Rabiatul_DrNazatulAini_Lab4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() //make UI go full screen
        setContent {
            A211684_Rabiatul_DrNazatulAini_Lab4Theme()  {
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
