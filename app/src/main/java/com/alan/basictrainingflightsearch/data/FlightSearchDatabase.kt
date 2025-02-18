package com.alan.basictrainingflightsearch.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Favorite::class], version = 1, exportSchema = true)
abstract class FlightSearchDatabase: RoomDatabase() {

    abstract fun airportDao(): AirportDao

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        @Volatile
        private var Instance: FlightSearchDatabase? = null

        fun getDatabase(context: Context): FlightSearchDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context = context,
                    klass = FlightSearchDatabase::class.java,
                    name = "flights"
                )
                    .fallbackToDestructiveMigration() //TODO: remove this when done with development
                    .createFromAsset("database/flight_search.db")
                    .build().also { Instance = it }
            }
        }
    }

}