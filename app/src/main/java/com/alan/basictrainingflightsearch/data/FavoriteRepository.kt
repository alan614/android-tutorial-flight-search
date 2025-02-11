package com.alan.basictrainingflightsearch.data

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteStream(id: Int): Flow<Favorite>

    fun getAllFavoritesStream(): Flow<List<Favorite>>

    fun getAllFavoritesByDepartureCodeStream(code: String): Flow<List<Favorite>>

    fun getAllFavoritesByArrivalCodeStream(code: String): Flow<List<Favorite>>

    fun getFavoriteByDepartureAndDestinationCodeStream(departureCode: String, destinationCode: String): Flow<Favorite>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun updateFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)

    suspend fun deleteByDepartureAndArrivalCodes(departureCode: String, destinationCode: String)
}