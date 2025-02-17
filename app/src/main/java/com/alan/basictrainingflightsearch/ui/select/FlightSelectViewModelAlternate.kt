package com.alan.basictrainingflightsearch.ui.select

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FlightSelectViewModelAlternate(
    val savedStateHandle: SavedStateHandle,
    val airportRepository: AirportRepository,
    val favoriteRepository: FavoriteRepository
): ViewModel() {

    private val airportCode: String = checkNotNull(savedStateHandle[FlightSelectDestination.airportArgId])

    //var airport by mutableStateOf(Airport(0, "", "", 0))
    private var _airport by mutableStateOf(Airport(id = 0, iataCode = "", name = "", passengers = 0))
    private var _favorites = emptyList<Favorite>()
    private var _destinations = emptyList<Airport>()

    private val _state = MutableStateFlow(FlightSelectViewModelAlternateUiState())
    val state = _state

    init {
        viewModelScope.launch {
            _airport = airportRepository.getAirportByCodeStream(airportCode).first()
            _favorites = favoriteRepository.getAllFavoritesByDepartureCodeStream(code = airportCode).first().toMutableList()
            _destinations = airportRepository.getAllAirportsExceptByCodeStream(exceptAirportCode = airportCode).first()

            state.update {
                it.copy(
                    airport = _airport,
                    favorites = _favorites,
                    destinations = _destinations,
                )
            }
        }
    }

    fun toggleFavorite(destination: Airport) {
        runBlocking {
            /*val thisFavorite = favoriteRepository
                .getFavoriteByDepartureAndDestinationCodeStream(departureCode = airportCode, destinationCode = destination.iataCode)
                .firstOrNull()*/

            val thisFavorite = state.value.favorites.firstOrNull { it.destinationCode == destination.iataCode }

            Log.d("TOGGLE_FAVORITE_AIRPORT", _airport.toString())
            Log.d("TOGGLE_FAVORITE_DESTINATION", destination.toString())
            Log.d("TOGGLE_FAVORITE_FAVORITE", thisFavorite.toString())

            if (thisFavorite != null) {
                Log.d("FAVORITE_ACTION", "removing")
                //favoriteRepository.deleteFavorite(thisFavorite)
                favoriteRepository.deleteByDepartureAndArrivalCodes(departureCode = airportCode, destinationCode = destination.iataCode)
                state.update {
                    val newFavorites = state.value.favorites.toMutableList()
                    newFavorites.remove(thisFavorite)
                    it.copy(
                        favorites = newFavorites
                    )
                }
            } else {
                Log.d("FAVORITE_ACTION", "adding")
                val newFavorite = Favorite(
                    id = 0,
                    departureCode = airportCode,
                    destinationCode = destination.iataCode
                )
                favoriteRepository.insertFavorite(
                    favorite = newFavorite
                )
                state.update {
                    val newFavorites = state.value.favorites.toMutableStateList()
                    newFavorites.add(newFavorite)
                    it.copy(
                        favorites = newFavorites
                    )
                }
            }

            Log.d("TOGGLE_FAVORITE_STATE", state.value.toString())
            /*_state.update {
                it.copy(
                    favorites = favoriteRepository.getAllFavoritesByDepartureCodeStream(code = airportCode).first()
                )
            }*/

            /*if (thisFavorite == null) {
                addFavorite(destination = destination)
            } else {
                removeFavorite(favorite = thisFavorite)
            }*/
        }
    }
}

data class FlightSelectViewModelAlternateUiState (
    val airport: Airport = Airport(id = 0, iataCode = "", name = "", passengers = 0),
    val destinations: List<Airport> = emptyList(),
    val favorites: List<Favorite> = emptyList(),
)