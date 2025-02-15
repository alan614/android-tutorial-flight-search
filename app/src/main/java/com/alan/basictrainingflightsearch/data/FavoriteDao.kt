package com.alan.basictrainingflightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(favorite: Favorite)

    @Update
    suspend fun update(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query(value = "SELECT * FROM favorite WHERE id = :id")
    fun getFavorite(id: Int): Flow<Favorite>

    @Query(value = "SELECT * FROM favorite where departure_code = :departureCode AND destination_code = :destinationCode")
    fun getFavoriteByDepartureAndArrivalCodes(departureCode: String, destinationCode: String): Flow<Favorite>?

    @Query(value = "SELECT * FROM favorite")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query(value = "SELECT * FROM favorite WHERE departure_code = :code")
    fun getFavoritesByDepartureCode(code: String): Flow<List<Favorite>>

    @Query(value = "SELECT * FROM favorite WHERE destination_code = :code")
    fun getFavoritesByArrivalCode(code: String): Flow<List<Favorite>>
}