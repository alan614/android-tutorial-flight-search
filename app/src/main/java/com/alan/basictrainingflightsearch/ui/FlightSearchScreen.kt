package com.alan.basictrainingflightsearch.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alan.basictrainingflightsearch.R
import com.alan.basictrainingflightsearch.data.Airport
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Composable
fun FlightSearchScreen() {
    Scaffold(
        topBar = {
            FlightTopBar()
        }
    ) { innerPadding ->
        FlightSearchBody(
            contentPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightTopBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.app_name)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        modifier = modifier,

    )
}

@Composable
fun FlightSearchBody(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel : FlightSearchViewModel = viewModel(factory = AppViewModelProvider.factory)
) {
    /*var searchTerm by remember {
        mutableStateOf("");
    }*/

    //val airports by viewModel.searchForAirport().collectAsState(emptyList())
    //val
    val searchTerm by viewModel.searchTerm.collectAsState()
    val airports by viewModel.airports.collectAsState(emptyList())

    Column(
        modifier = modifier.padding(contentPadding)
            .fillMaxWidth()
            //.border(width = 8.dp, color = MaterialTheme.colorScheme.primaryContainer, shape = ShapeDefaults.Medium)
    ) {
        TextField(
            value = searchTerm,
            onValueChange = {
                viewModel.searchTerm.value = it
                //viewModel.updateSearchTerms(it)
                //viewModel.searchForAirport()
                //airports = viewModel.searchForAirport()
            },
            label = { Text("Search for flight name") },
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                .padding(all = 8.dp),
        )

        /*val airports: List<Airport> = listOf(
            Airport(id = 1, iataCode = "abc", name = "ABC", passengers = 42),
            Airport(id = 2, iataCode = "def", name = "DEF", passengers = 15),
            Airport(id = 3, iataCode = "ha1", name = "Hello", passengers = 30),
            Airport(id = 4, iataCode = "ha2", name = "How Are you", passengers = 13),
            Airport(id = 5, iataCode = "ha3", name = "Quite well", passengers = 50),
        )*/



        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items (
                items = airports,
                key = { airport -> airport.id }
            ) {
                AirportCard(
                    airport = it,
                )
            }
        }
    }
}

@Composable
fun AirportCard(
    airport: Airport,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth()
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
        /*Card(
            shape = ShapeDefaults.ExtraLarge,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = airport.name)
        }*/
    }
}