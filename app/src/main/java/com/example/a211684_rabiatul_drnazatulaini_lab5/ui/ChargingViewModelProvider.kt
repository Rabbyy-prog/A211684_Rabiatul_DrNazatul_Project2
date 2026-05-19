package com.example.a211684_rabiatul_drnazatulaini_lab5.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a211684_rabiatul_drnazatulaini_lab5.ElecTraxApplication

object ChargingViewModelProvider {

    val Factory = viewModelFactory {

        initializer {

            ChargingViewModel(
                (this[APPLICATION_KEY] as ElecTraxApplication)
                    .container
                    .chargingHistoryRepository
            )
        }
    }
}