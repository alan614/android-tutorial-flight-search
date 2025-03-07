package com.alan.basictrainingflightsearch.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alan.basictrainingflightsearch.FlightSearchApplication
import com.alan.basictrainingflightsearch.ui.home.HomeViewModel
import com.alan.basictrainingflightsearch.ui.select.FlightSelectViewModel

object AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            HomeViewModel(
                //savedStateHandle = this.createSavedStateHandle(),
                airportRepository = flightSearchApplication().container.airportRepository,
                favoriteRepository = flightSearchApplication().container.favoriteRepository,
                userPreferencesRepository = flightSearchApplication().userPreferencesRepository,
            )
        }

        initializer {
            FlightSelectViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                airportRepository = flightSearchApplication().container.airportRepository,
                favoriteRepository = flightSearchApplication().container.favoriteRepository,
            )
        }
    }
}

fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as FlightSearchApplication