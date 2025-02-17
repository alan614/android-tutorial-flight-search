package com.alan.basictrainingflightsearch.ui.select

import android.util.Log
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSelectViewModel(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val airportCode: String = checkNotNull(savedStateHandle[FlightSelectDestination.airportArgId])

    var airport by mutableStateOf(Airport(0, "", "", 0))

    private val _favorites = favoriteRepository
        .getAllFavoritesByDepartureCodeStream(code = airportCode)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000L),
            initialValue = emptyList()
        )

    private val _airports = airportRepository
        .getAllAirportsExceptByCodeStream(exceptAirportCode = airportCode)
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

    init {
        viewModelScope.launch {
            airport = airportRepository
                .getAirportByCodeStream(airportCode)
                .filterNotNull()
                .first()
        }
    }

    suspend fun addFavorite(destination: Airport) {
        val thisFavorite = Favorite(
            id = 0,
            departureCode = airport.iataCode,
            destinationCode = destination.iataCode
        )

        favoriteRepository.insertFavorite(thisFavorite)
    }

    suspend fun removeFavorite(favorite: Favorite) {
        favoriteRepository.deleteFavorite(favorite = favorite)
    }

    fun toggleFavorite(destination: Airport) {
        viewModelScope.launch {
            /*val thisFavorite = favoriteRepository
                .getFavoriteByDepartureAndDestinationCodeStream(departureCode = airport.iataCode, destinationCode = destination.iataCode)
                .firstOrNull()*/

            val targetFavorite = _favorites.value.find {
               it.destinationCode == destination.iataCode
            }

            Log.d("TOGGLE_FAVORITE_AIRPORT", airport.toString())
            Log.d("TOGGLE_FAVORITE_DESTINATION", destination.toString())
            Log.d("TOGGLE_FAVORITE_FAVORITE", targetFavorite.toString())

            if (targetFavorite == null) {
                addFavorite(destination = destination)
            } else {
                //removeFavorite(favorite = targetFavorite)
                favoriteRepository.deleteByDepartureAndArrivalCodes(departureCode = airportCode, destinationCode = destination.iataCode)
            }

            /*_state.update {
                _state.value.copy(
                    favorites = favoriteRepository
                        .getAllFavoritesByDepartureCodeStream(code = airportCode)
                        .first()
                )
            }*/
        }
    }
}

data class FlightSelectUiState(
    //val airport: Airport = Airport(id = 0, iataCode = "",name = "",passengers = 0),
    val destinations: List<Airport> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
)