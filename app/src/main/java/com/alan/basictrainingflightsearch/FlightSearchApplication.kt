package com.alan.basictrainingflightsearch

import android.app.Application
import com.alan.basictrainingflightsearch.data.AppContainer
import com.alan.basictrainingflightsearch.data.AppDataContainer

class FlightSearchApplication: Application() {

    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}