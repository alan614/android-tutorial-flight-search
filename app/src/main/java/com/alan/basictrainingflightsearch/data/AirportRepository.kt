package com.alan.basictrainingflightsearch.data

import kotlinx.coroutines.flow.Flow

interface AirportRepository {
    fun getAirportStream(id: Int): Flow<Airport>

    fun getAllAirportsStream(): Flow<List<Airport>>

    fun getAllAirportsExceptStream(exceptAirportId: Int): Flow<List<Airport>>

    fun searchAirportStream(searchTerm: String): Flow<List<Airport>>

    suspend fun insertAirport(airport: Airport)

    suspend fun updateAirport(airport: Airport)

    suspend fun deleteAirport(airport: Airport)
}