package com.alan.basictrainingflightsearch.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.data.Favorite
import com.alan.basictrainingflightsearch.data.FavoriteFlight
import com.alan.basictrainingflightsearch.ui.AppViewModelProvider
import com.alan.basictrainingflightsearch.ui.FlightTopBar
import com.alan.basictrainingflightsearch.ui.navigation.NavigationDestination
import java.text.NumberFormat

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
                    favoriteFlights = state.favorites,
                    removeFavorite = { viewModel.removeFlight(it) }
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 16.dp)
        ) {

            Column(
                modifier = Modifier./*padding(all = 16.dp).*/weight(1f)
            ) {
                Text(
                    text = airport.iataCode,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = airport.name
                )
            }

            Spacer(modifier = Modifier.weight(0.1f))

            Text(text = "${NumberFormat.getInstance().format(airport.passengers)}/year")
        }
    }
}

@Composable
fun FavoritesList(
    favoriteFlights: List<FavoriteFlight>,
    removeFavorite: (FavoriteFlight) -> Unit,
    modifier: Modifier = Modifier
) {
    if (favoriteFlights.isEmpty()) {
        Text(text = "No Favorites found")
    } else {
        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier,
        ) {
            items (
                items = favoriteFlights,
                key = { favorite -> favorite.id }
            ) {
                FavoriteFlightCard(
                    favoriteFlight = it,
                    removeFavorite = removeFavorite
                )
            }
        }
    }
}

@Composable
fun FavoriteFlightCard(
    favoriteFlight: FavoriteFlight,
    removeFavorite: (FavoriteFlight) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                //add code to delete favorite
                removeFavorite(favoriteFlight)
            }
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Depart",
                    style = MaterialTheme.typography.labelSmall,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    Text(
                        text = favoriteFlight.departureCode,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = favoriteFlight.departureName,
                    )
                }

                Text(
                    text = "Arrive",
                    style = MaterialTheme.typography.labelSmall,
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    Text(
                        text = favoriteFlight.destinationCode,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = favoriteFlight.destinationName
                    )
                }
            }

            Spacer(modifier = Modifier.weight(weight = 0.1f))

            Icon(
                imageVector = Icons.Outlined.Favorite,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Mark as favorite",
                modifier = Modifier
                    .height(64.dp)
                    .width(64.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AirportListPreview() {

    val myAirports = listOf(
        Airport(
            id = 1,
            iataCode = "ABA",
            name = "Abadaba",
            passengers = 452111
        ),
        Airport(
            id = 2,
            iataCode = "WAR",
            name = "Warsaw",
            passengers = 100
        ),
    )

    AirportList(
        airports = myAirports,
        onAirportClick = {},

    )
}