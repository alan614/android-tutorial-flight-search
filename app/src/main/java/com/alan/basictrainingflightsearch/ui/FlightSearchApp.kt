package com.alan.basictrainingflightsearch.ui

import android.util.Log
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alan.basictrainingflightsearch.R
import com.alan.basictrainingflightsearch.ui.navigation.FlightNavGraph

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
fun FlightSearchApp() {

    val navController: NavHostController = rememberNavController()
    FlightNavGraph(
        navController = navController
    )
}