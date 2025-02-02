package com.alan.basictrainingflightsearch.ui

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alan.basictrainingflightsearch.R

enum class FlightSearchScreens{
    Search,
    SelectFlight,
    ShowFavorites,
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
fun FlightSearchApp(
    viewModel: FlightSearchViewModel = viewModel(factory = AppViewModelProvider.factory)
) {

    val navController: NavHostController = rememberNavController()

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            FlightTopBar()
        }
    ) { innerPadding ->
        /*FlightSearchBody(
            contentPadding = innerPadding
        )*/

        NavHost(
            navController = navController,
            startDestination = FlightSearchScreens.Search.name
        ) {
            composable(FlightSearchScreens.Search.name) {
                /*FullScheduleScreen(
                    busSchedules = fullSchedule,
                    contentPadding = innerPadding,
                    onScheduleClick = { busStopName ->
                        navController.navigate(
                            "${BusScheduleScreens.RouteSchedule.name}/$busStopName"
                        )
                        topAppBarTitle = busStopName
                    }
                )*/
                FlightSearchBody(
                    onAirportClick = {
                        viewModel.setCurrentAirportId(it)
                        navController.navigate(FlightSearchScreens.SelectFlight.name)
                    },
                    contentPadding = innerPadding
                )
            }

            composable(FlightSearchScreens.SelectFlight.name) {
                SelectFlightScreen(
                    baseAirportId = uiState.currentAirportId,
                    contentPadding = innerPadding
                )
            }
            /*val busRouteArgument = "busRoute"
            composable(
                route = BusScheduleScreens.RouteSchedule.name + "/{$busRouteArgument}",
                arguments = listOf(navArgument(busRouteArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val stopName = backStackEntry.arguments?.getString(busRouteArgument)
                    ?: error("busRouteArgument cannot be null")
                val routeSchedule by viewModel.getScheduleFor(stopName).collectAsState(emptyList())
                RouteScheduleScreen(
                    stopName = stopName,
                    busSchedules = routeSchedule,
                    contentPadding = innerPadding,
                    onBack = { onBackHandler() }
                )
            }*/
        }
    }
}