package com.alan.basictrainingflightsearch.ui

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

enum class FlightSearchScreens{
    Search,
    SelectFlight,
    ShowFavorites,
}

@Composable
fun FlightSearchApp(
    viewModel: FlightSearchViewModel = viewModel(factory = AppViewModelProvider.factory)
) {

    val navController: NavHostController = rememberNavController()

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