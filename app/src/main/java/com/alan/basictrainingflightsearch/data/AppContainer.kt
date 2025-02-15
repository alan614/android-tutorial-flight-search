package com.alan.basictrainingflightsearch.data

import android.content.Context

interface AppContainer {
    val airportRepository: AirportRepository
    val favoriteRepository: FavoriteRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val airportRepository: AirportRepository by lazy {
        OfflineAirportRepository(FlightSearchDatabase.getDatabase(context).airportDao())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        OfflineFavoriteRepository(FlightSearchDatabase.getDatabase(context).favoriteDao())
    }
}