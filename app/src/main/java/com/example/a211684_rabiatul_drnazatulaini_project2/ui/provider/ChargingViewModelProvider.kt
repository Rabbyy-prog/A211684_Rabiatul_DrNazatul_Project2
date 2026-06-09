package com.example.a211684_rabiatul_drnazatulaini_project2.ui.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a211684_rabiatul_drnazatulaini_project2.ElecTraxApplication
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel.ChargingViewModel

//handle charging input logic
object ChargingViewModelProvider {

    val Factory = viewModelFactory {

        initializer {

            ChargingViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as ElecTraxApplication)
                    .container
                    .chargingHistoryRepository
            )
        }
    }
}