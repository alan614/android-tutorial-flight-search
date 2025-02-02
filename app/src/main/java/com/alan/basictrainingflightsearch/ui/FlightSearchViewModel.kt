package com.alan.basictrainingflightsearch.ui

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

class FlightSearchViewModel(
    private val airportRepository: AirportRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    val searchTerm = MutableStateFlow("")

    //val currentAirport = MutableStateFlow(0);

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val airports = searchTerm
        .debounce(timeoutMillis = 1_000)
        .distinctUntilChanged()
        .flatMapLatest {
            searchForAirport(it)
        }

    //var searchTerm by mutableStateOf("")

    /*fun updateSearchTerms(terms: String) {
        searchTerm = terms
    }*/

    fun getAirport(id: Int) : Flow<Airport> = airportRepository.getAirportStream(id = id)

    fun getAllAirports() : Flow<List<Airport>> = airportRepository.getAllAirportsStream()

    fun setCurrentAirportId(airportId: Int) {
        _uiState.update {
            it.copy(currentAirportId = airportId)
        }
    }

    private fun searchForAirport(searchString: String) : Flow<List<Airport>> {
        return airportRepository.searchAirportStream("%${searchString}%")
    }

    fun getAirportsExcept(exceptIdAirportId: Int) : Flow<List<Airport>> = airportRepository.getAllAirportsExceptStream(exceptAirportId = exceptIdAirportId)
}


data class UiState(
    //val searchTerm: String = "",
    val currentAirportId: Int = 0,
)
