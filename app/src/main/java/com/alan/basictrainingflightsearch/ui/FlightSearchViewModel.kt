package com.alan.basictrainingflightsearch.ui

import android.util.Log
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val searchTerm = MutableStateFlow("")

    //val currentAirport = MutableStateFlow(0);

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val airports = searchTerm
        .debounce(timeoutMillis = 1_000L)
        .distinctUntilChanged()
        .flatMapLatest {
            searchForAirport(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
    //var searchTerm by mutableStateOf("")

    /*fun updateSearchTerms(terms: String) {
        searchTerm = terms
    }*/

    val favoritesState: StateFlow<FavoritesState> = favoriteRepository
        .getAllFavoritesStream()
        .map { FavoritesState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = FavoritesState()
        )

    fun getAirport(id: Int) : Flow<Airport> = airportRepository.getAirportStream(id = id)

    //fun getAllAirports() : Flow<List<Airport>> = airportRepository.getAllAirportsStream()

    fun setCurrentAirportId(airportId: Int) {
        _uiState.update {
            it.copy(currentAirportId = airportId)
        }
    }

    private fun searchForAirport(searchString: String) : Flow<List<Airport>> {
        val queryTerm: String = if (searchString == "") {
            ""
        } else {
            "%${searchString}%"
        }
        return airportRepository.searchAirportStream(queryTerm)
    }

    fun getAirportsExcept(exceptIdAirportId: Int) : Flow<List<Airport>> = airportRepository.getAllAirportsExceptStream(exceptAirportId = exceptIdAirportId)

    fun getFavorites(): Flow<List<Favorite>> {
        return favoriteRepository.getAllFavoritesStream()
    }

    fun getFavoritesWithDeparture(code: String): Flow<List<Favorite>> {
        return favoriteRepository.getAllFavoritesByDepartureCode(code = code)
    }

    suspend fun addFavoriteByAirports(departureAirport: Airport, destinationAirport: Airport) {
        Log.d("FAVORITE_ADD", "${departureAirport.iataCode} - ${destinationAirport.iataCode}")
        //viewModelScope.launch {
            val favorite = Favorite(
                id = 0,
                departureCode = departureAirport.iataCode,
                destinationCode = destinationAirport.iataCode
            )
            favoriteRepository.insertFavorite(favorite)
        //}
    }

    suspend fun removeFavorite(favorite: Favorite) {
        //viewModelScope.launch {
            favoriteRepository.deleteFavorite(favorite)
        //}
    }

    suspend fun toggleFavoriteByDepartureAirport(departureAirport: Airport, destinationAirport: Airport) {
            val favorite = favoriteRepository
                .getFavoriteByDepartureAndDestinationCodeStream(departureCode = departureAirport.iataCode, destinationCode = destinationAirport.iataCode)
                ?.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
                ?.value

            if (favorite == null) {
                addFavoriteByAirports(departureAirport = departureAirport, destinationAirport = destinationAirport)
            } else {
                removeFavorite(favorite)
            }
    }
}


data class UiState(
    //val searchTerm: String = "",
    val currentAirportId: Int = 0,
    val favoritesList: List<Favorite> = listOf()
)

data class FavoritesState(
    val favoritesList: List<Favorite> = emptyList()
)