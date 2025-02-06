package com.alan.basictrainingflightsearch.data

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getFavoriteStream(id: Int): Flow<Favorite>

    fun getFavoriteByDepartureAndDestinationCodeStream(departureCode: String, destinationCode: String): Flow<Favorite>?

    fun getAllFavoritesStream(): Flow<List<Favorite>>

    fun getAllFavoritesByDepartureCode(code: String): Flow<List<Favorite>>

    fun getAllFavoritesByArrivalCode(code: String): Flow<List<Favorite>>

    suspend fun insertFavorite(favorite: Favorite)

    suspend fun updateFavorite(favorite: Favorite)

    suspend fun deleteFavorite(favorite: Favorite)
}