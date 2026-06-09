package com.example.a211684_rabiatul_drnazatulaini_project2.ui.utils

import com.example.a211684_rabiatul_drnazatulaini_project2.R

fun getStationImage(name: String): Int {
    return when {
        name.contains("Gateway") -> R.drawable.ev_chargers___bangi_gateway_shopping_mall
        name.contains("Christine") -> R.drawable.christine_bangi_avenue_evcharger
        name.contains("Lotus") -> R.drawable.lotus_bandar_puteri_bangi_ev_charger
        name.contains("Tenera Hotel Bangi") -> R.drawable.tenera_hotel_bangi_evcharger
        name.contains("Bangi Gold Resort") -> R.drawable.bangi_golf_resort
        else -> R.drawable.ev_chargers___bangi_gateway_shopping_mall
    }
}