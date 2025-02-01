package com.alan.basictrainingflightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineAirportRepository(private val airportDao: AirportDao): AirportRepository {
    override fun getAirportStream(id: Int): Flow<Airport> = airportDao.getAirport(id = id)

    override fun getAllAirportsStream(): Flow<List<Airport>> = airportDao.getAllAirports()

    override fun getAllAirportsExceptStream(exceptAirportId: Int): Flow<List<Airport>> = airportDao.getAllAirportsExcept(exceptAirportId = exceptAirportId)

    override fun searchAirportStream(searchTerm: String): Flow<List<Airport>> = airportDao.searchAirport(searchTerm = searchTerm)

    override suspend fun insertAirport(airport: Airport) = airportDao.insert(airport = airport)

    override suspend fun updateAirport(airport: Airport) = airportDao.update(airport = airport)

    override suspend fun deleteAirport(airport: Airport) = airportDao.delete(airport = airport)

}