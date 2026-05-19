package com.example.a211684_rabiatul_drnazatulaini_lab5

import android.app.Application
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.AppContainer
import com.example.a211684_rabiatul_drnazatulaini_lab5.data.AppDataContainer

class ElecTraxApplication: Application(){

    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppDataContainer(this)

    }
}