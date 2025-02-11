package com.alan.basictrainingflightsearch.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteRepository

class FlightSelectViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val airportId: Int = checkNotNull(savedStateHandle[FlightSelectDestination.airportArgId])

}

data class SelectFlightUiState(
    val airport: Airport = Airport(id = 0, iataCode = "",name = "",passengers = 0),
    val destinations: List<Airport> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
)