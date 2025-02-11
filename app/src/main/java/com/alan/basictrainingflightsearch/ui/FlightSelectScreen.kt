package com.alan.basictrainingflightsearch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alan.basictrainingflightsearch.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object FlightSelectDestination: NavigationDestination {
    override val route: String = "flight_select"
    override val title: String = "Select Flight"
    const val airportArgId: String = "airportId"
    val routeWithArgs: String = "$route/{$airportArgId}"
}

@Composable
fun FlightSelectScreen(
    modifier: Modifier = Modifier,
    viewModel: FlightSelectViewModel = viewModel(factory = AppViewModelProvider.factory)
) {

    val state by viewModel.state.collectAsState(initial = FlightSelectUiState())
    val airport = viewModel.airport

    Scaffold(
        topBar = {
            FlightTopBar()
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            //horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Choose your favorite flights",
                    textAlign = TextAlign.Center,
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                items(
                    items = state.destinations,
                    key = { airport -> airport.id }
                ) { destination ->
                    FlightCard(
                        airportDeparture = airport,
                        airportDestination = destination,
                        onFlightClick = {
                            /*coroutineScope.launch {
                                viewModel.toggleFavoriteByDepartureAirport(
                                    departureAirport = airport,
                                    destinationAirport = destination
                                )
                            }*/
                        },
                        isFavorite = (state.favorites.find { favorite -> favorite.departureCode == airport.iataCode && favorite.destinationCode == destination.iataCode } != null)
                    )
                }
            }
        }
    }
}