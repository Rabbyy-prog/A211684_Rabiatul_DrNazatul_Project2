package com.example.a211684_rabiatul_drnazatulaini_project2.ui.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.a211684_rabiatul_drnazatulaini_project2.ElecTraxApplication
import com.example.a211684_rabiatul_drnazatulaini_project2.ui.viewModel.ChargingHistoryViewModel

//handle charging history data
object ChargingHistoryViewModelProvider {

    val Factory = viewModelFactory { //teach android how to create ChargingHistoryViewModel

        initializer {

            ChargingHistoryViewModel(
                (this[ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY] as ElecTraxApplication) //get application object & get container object from application object
                    .container
                    .chargingHistoryRepository
            )
        }
    }
}