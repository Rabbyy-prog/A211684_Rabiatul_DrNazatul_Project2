package com.example.a211684_rabiatul_drnazatulaini_project2

import android.app.Application
import com.example.a211684_rabiatul_drnazatulaini_project2.di.AppContainer
import com.example.a211684_rabiatul_drnazatulaini_project2.di.AppDataContainer

class ElecTraxApplication: Application(){

    lateinit var container: AppContainer

    override fun onCreate(){
        super.onCreate()
        container = AppDataContainer(this)

    }
}