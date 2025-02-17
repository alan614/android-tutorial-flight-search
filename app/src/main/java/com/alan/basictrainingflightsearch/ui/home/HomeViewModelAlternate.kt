package com.alan.basictrainingflightsearch.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.AirportRepository
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModelAlternate(
    airportRepository: AirportRepository,
    favoriteRepository: FavoriteRepository,
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val _airports = _searchQuery
        .debounce(timeoutMillis = 1_000L)
        .flatMapLatest {

            val query = if (it.isNotBlank()) {
                "%${it}%"
            } else {
                it
            }

            favoriteRepository.getAllFavoritesStream()
            airportRepository.searchAirportStream(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = emptyList()
        )

    private var _favorites = emptyList<Favorite>()

    private val _state = MutableStateFlow(HomeUiState())

    val state = combine(_state, _searchQuery, _airports, /*_favorites*/) { state, searchQuery, airports, /*favorites*/ ->
        state.copy(
            searchQuery = searchQuery,
            airports = airports,
            //favorites = favorites,
        )
    }

    init {
        viewModelScope.launch {
            _favorites = favoriteRepository.getAllFavoritesStream().first()
            _state.update {
                it.copy(
                    favorites = _favorites
                )
            }
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        _searchQuery.update {
            searchQuery
        };
    }
}