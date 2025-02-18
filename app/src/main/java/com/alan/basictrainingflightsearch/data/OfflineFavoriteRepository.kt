package com.alan.basictrainingflightsearch.data

import kotlinx.coroutines.flow.Flow

class OfflineFavoriteRepository(private val favoriteDao: FavoriteDao): FavoriteRepository {
    override fun getFavoriteStream(id: Int): Flow<Favorite> = favoriteDao.getFavorite(id = id)

    override fun getFavoriteByDepartureAndDestinationCodeStream(
        departureCode: String,
        destinationCode: String
    ): Flow<Favorite> = favoriteDao.getFavoriteByDepartureAndArrivalCodesStream(departureCode = departureCode, destinationCode = destinationCode)

    override fun getFavoriteFlightStream(id: Int): Flow<FavoriteFlight> = favoriteDao.getFavoriteFlight(id = id)

    override fun getAllFavoriteFlightsStream(): Flow<List<FavoriteFlight>> = favoriteDao.getAllFavoriteFlights()

    override fun getAllFavoritesStream(): Flow<List<Favorite>> = favoriteDao.getAllFavorites()

    override fun getAllFavoritesByDepartureCodeStream(code: String): Flow<List<Favorite>> = favoriteDao.getFavoritesByDepartureCode(code = code)

    override fun getAllFavoritesByArrivalCodeStream(code: String): Flow<List<Favorite>> = favoriteDao.getFavoritesByArrivalCode(code = code)

    override suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insert(favorite = favorite)

    override suspend fun updateFavorite(favorite: Favorite) = favoriteDao.update(favorite = favorite)

    override suspend fun deleteFavorite(favorite: Favorite) = favoriteDao.delete(favorite = favorite)

    override suspend fun deleteByDepartureAndArrivalCodes(
        departureCode: String,
        destinationCode: String
    ) = favoriteDao.deleteByDepartureAndArrivalCodes(departureCode = departureCode, destinationCode = destinationCode)

}