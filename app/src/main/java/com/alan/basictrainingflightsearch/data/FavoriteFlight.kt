package com.alan.basictrainingflightsearch.data

import androidx.room.PrimaryKey

data class FavoriteFlight(
    @PrimaryKey val id: Int,
    val departureName: String,
    val departureCode: String,
    val destinationName: String,
    val destinationCode: String,
)

fun FavoriteFlight.toFavorite(): Favorite = Favorite(
    id = id,
    departureCode = departureCode,
    destinationCode = destinationCode
)