package com.alan.basictrainingflightsearch.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteFlight
import com.alan.basictrainingflightsearch.ui.AppViewModelProvider
import com.alan.basictrainingflightsearch.ui.FlightTopBar
import com.alan.basictrainingflightsearch.ui.navigation.NavigationDestination

object HomeDestination: NavigationDestination {
    override val route: String = "home"
    override val title: String = "Flight Search"
}

@Composable
fun HomeScreen(
    onSelectAirport: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    val state by viewModel.state.collectAsState(initial = HomeUiState())

    Scaffold(
        topBar = {
            FlightTopBar()
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            TextField(
                value = state.searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                },
                label = { Text("Search for flight name") },
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(all = 8.dp),
            )

            if (state.searchQuery.isBlank()) {
                FavoritesList(
                    favorites = state.favorites
                )
            } else {
                AirportList(
                    airports = state.airports,
                    onAirportClick = onSelectAirport
                )
            }
        }
    }
}

@Composable
fun AirportList(
    airports: List<Airport>,
    onAirportClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        items (
            items = airports,
            key = { airport -> airport.id }
        ) {
            AirportCard(
                airport = it,
                onAirportClick = onAirportClick
            )
        }
    }
}

@Composable
fun AirportCard(
    airport: Airport,
    onAirportClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onAirportClick(airport.iataCode)
            }
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Text(
                text = airport.iataCode,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = airport.name
            )
        }
    }
}

@Composable
fun FavoritesList(
    favorites: List<FavoriteFlight>,
    modifier: Modifier = Modifier
) {
    if (favorites.isEmpty()) {
        Text(text = "No Favorites found")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier,
        ) {
            items (
                items = favorites,
                key = { favorite -> favorite.id }
            ) {
                Column {
                    Text(text = it.departureName)
                    Text(text = it.destinationName)
                }
            }
        }
    }

}