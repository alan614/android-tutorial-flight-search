package com.alan.basictrainingflightsearch.ui

import com.alan.basictrainingflightsearch.ui.navigation.NavigationDestination

object FlightSelectDestination: NavigationDestination {
    override val route: String = "flight_select"
    override val title: String = "Select Flight"
    const val airportArgId: String = "airportId"
    val routeWithArgs: String = "$route/{$airportArgId}"
}