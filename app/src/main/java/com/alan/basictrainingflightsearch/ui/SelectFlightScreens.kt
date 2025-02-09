package com.alan.basictrainingflightsearch.ui

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alan.basictrainingflightsearch.data.Airport
import com.alan.basictrainingflightsearch.ui.theme.BasicTrainingFlightSearchTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun SelectFlightScreen(
    baseAirportId: Int,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(all = 8.dp),
    viewModel: FlightSearchViewModel = viewModel(factory = AppViewModelProvider.factory),
) {

    val airport by viewModel
        .getAirport(baseAirportId)
        .collectAsState(
            initial = Airport(
                id = 0,
                name = "",
                iataCode = "",
                passengers = 0
            )
        )

    val airports by viewModel
        .getAirportsExcept(exceptIdAirportId = airport.id)
        .collectAsState(emptyList())

    val airportFavorites by viewModel
        .getFavoritesWithDeparture(code = airport.iataCode)
        .collectAsState(emptyList())

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.padding(contentPadding),
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
                items = airports,
                key = { airport -> airport.id }
            ) { destination ->
                FlightCard(
                    airportDeparture = airport,
                    airportDestination = destination,
                    onFlightClick = {
                        coroutineScope.launch {
                            viewModel.toggleFavoriteByDepartureAirport(
                                departureAirport = airport,
                                destinationAirport = destination
                            )
                        }
                    },
                    isFavorite = (airportFavorites.find { favorite -> favorite.departureCode == airport.iataCode && favorite.destinationCode == destination.iataCode } != null)
                )
            }
        }
    }
}

@Composable
fun FlightCard(
    airportDeparture: Airport,
    airportDestination: Airport,
    onFlightClick: (Int) -> Unit,
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth().clickable {
            onFlightClick(airportDeparture.id)
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
                        text = airportDeparture.iataCode,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = airportDeparture.name
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
                        text = airportDestination.iataCode,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = airportDestination.name
                    )
                }
            }

            Spacer(modifier = Modifier.weight(weight = 0.1f))

            Icon(
                imageVector = if (isFavorite) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Mark as favorite",
                modifier = Modifier.height(64.dp).width(64.dp),
            )
        }
        /*Card(
            shape = ShapeDefaults.ExtraLarge,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = airport.name)
        }*/
    }
}

@Preview(showBackground = true)
@Composable
fun SelectFlightPreview() {
    BasicTrainingFlightSearchTheme {

        val departure: Airport = Airport(
            id = 0,
            name = "Testing 1 initial departure area Warsaw",
            iataCode = "TST1",
            passengers = 50
        )
        val destination: Airport = Airport(
            id = 0,
            name = "New Area final destination area Australia",
            iataCode = "NA1",
            passengers = 40
        )

        FlightCard(
            airportDeparture = departure,
            airportDestination = destination,
            onFlightClick = {},
        )
    }
}
@Preview(showBackground = true)
@Composable
fun SelectFlightPreviewSelected() {
    BasicTrainingFlightSearchTheme {

        val departure: Airport = Airport(
            id = 0,
            name = "Testing 1 initial departure area Warsaw",
            iataCode = "TST1",
            passengers = 50
        )
        val destination: Airport = Airport(
            id = 0,
            name = "New Area final destination area Australia",
            iataCode = "NA1",
            passengers = 40
        )

        FlightCard(
            airportDeparture = departure,
            airportDestination = destination,
            onFlightClick = {},
            isFavorite = true,
        )
    }
}
