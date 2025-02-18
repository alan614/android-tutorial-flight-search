package com.alan.basictrainingflightsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(airport: Airport)

    @Update
    suspend fun update(airport: Airport)

    @Update
    suspend fun delete(airport: Airport)

    @Query(value = "SELECT * FROM airport WHERE id = :id")
    fun getAirport(id: Int): Flow<Airport>

    @Query(value = "SELECT * FROM airport WHERE iata_code = :code")
    fun getAirportByCode(code: String) : Flow<Airport>

    @Query(value = "SELECT * FROM airport WHERE name LIKE :searchTerm OR iata_code LIKE :searchTerm")
    fun searchAirport(searchTerm: String): Flow<List<Airport>>

    @Query(value = "SELECT * FROM airport ORDER BY passengers DESC")
    fun getAllAirports(): Flow<List<Airport>>

    @Query(value = "SELECT * FROM airport WHERE id <> :exceptAirportId ORDER BY passengers DESC")
    fun getAllAirportsExcept(exceptAirportId: Int): Flow<List<Airport>>

    @Query(value = "SELECT * FROM airport WHERE iata_code <> :exceptAirportCode ORDER BY passengers DESC")
    fun getAllAirportsExceptByCode(exceptAirportCode: String): Flow<List<Airport>>
}