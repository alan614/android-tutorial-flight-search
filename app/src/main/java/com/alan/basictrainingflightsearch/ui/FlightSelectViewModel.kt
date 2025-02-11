package com.alan.basictrainingflightsearch.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSelectViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val airportId: Int = checkNotNull(savedStateHandle[FlightSelectDestination.airportArgId])

    var airport by mutableStateOf(Airport(0, "", "", 0))
    /*val airport by lazy {
        viewModelScope.launch {
            airportRepository
                .getAirportStream(airportId)
                .filterNotNull()
                .first()
        }
    }*/

    init {
        viewModelScope.launch {
            airport = airportRepository
                .getAirportStream(airportId)
                .filterNotNull()
                .first()
        }
    }

    private val _favorites = favoriteRepository
        .getAllFavoritesStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = emptyList()
        )

    private val _airports = airportRepository
        .getAllAirportsExceptStream(exceptAirportId = airportId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(FlightSelectUiState())

    val state = combine(_state, _favorites, _airports) { state, favorites, airports ->
        state.copy(
            destinations = airports,
            favorites = favorites,
        )
    }
}

data class FlightSelectUiState(
    //val airport: Airport = Airport(id = 0, iataCode = "",name = "",passengers = 0),
    val destinations: List<Airport> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
)