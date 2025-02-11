package com.alan.basictrainingflightsearch.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alan.basictrainingflightsearch.ui.FlightSelectDestination
import com.alan.basictrainingflightsearch.ui.HomeDestination
import com.alan.basictrainingflightsearch.ui.HomeScreen

@Composable
fun FlightNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier,
    ) {

        composable(route = HomeDestination.route) {
            HomeScreen(
                onSelectAirport = {
                    navController.navigate(route = "${FlightSelectDestination.route}/${it}")
                }
            )
        }

        composable(
            route = FlightSelectDestination.routeWithArgs,
            arguments = listOf(navArgument(name = FlightSelectDestination.airportArgId) {
                type = NavType.IntType
            })
        ) {
            Text("testing")
        }
    }
}
