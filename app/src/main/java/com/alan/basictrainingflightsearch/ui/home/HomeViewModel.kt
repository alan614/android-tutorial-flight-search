package com.alan.basictrainingflightsearch.ui.home

import android.util.Log
import androidx.compose.runtime.key
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteFlight
import com.alan.basictrainingflightsearch.data.FavoriteRepository
import com.alan.basictrainingflightsearch.data.UserPreferencesRepository
import com.alan.basictrainingflightsearch.data.toFavorite
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HomeViewModel(
    //savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    init {
        /*viewModelScope.launch {
            userPreferencesRepository.keywords.collectLatest { keyword ->
                _searchQuery.update {
                    keyword
                }
            }
        }*/

        runBlocking {
            _searchQuery.update {
                userPreferencesRepository.keywords.first()
            }
        }

        viewModelScope.launch {
            _searchQuery.debounce(1_000L).collectLatest {
                userPreferencesRepository.saveKeywordsPreferences(it)
            }
        }
    }


    private val _airports = _searchQuery
        .debounce(timeoutMillis = 1_000L)
        .flatMapLatest {

            val query = if (it.isNotBlank()) {
                "%${it}%"
            } else {
                it
            }

            //
            airportRepository.searchAirportStream(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = emptyList()
        )

    private val _favorites = favoriteRepository
        .getAllFavoriteFlightsStream()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(HomeUiState())

    val state = combine(_state, _searchQuery, _airports, _favorites) { state, searchQuery, airports, favorites ->
        state.copy(
            searchQuery = searchQuery,
            airports = airports,
            favorites = favorites,
        )
    }

    fun updateSearchQuery(searchQuery: String) {
        _searchQuery.update {
            searchQuery
        }
    }

    fun removeFlight(favoriteFlight: FavoriteFlight) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favoriteFlight.toFavorite())
        }
    }
}

data class HomeUiState(
    val searchQuery: String = "",
    val airports: List<Airport> = emptyList(),
    val favorites: List<FavoriteFlight> = emptyList(),
)