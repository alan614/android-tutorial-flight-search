package com.alan.basictrainingflightsearch.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alan.basictrainingflightsearch.FlightSearchApplication

object AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            FlightSearchViewModel(
                flightSearchApplication().container.airportRepository,
                flightSearchApplication().container.favoriteRepository,
            )
        }
    }
}

fun CreationExtras.flightSearchApplication(): FlightSearchApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as FlightSearchApplication