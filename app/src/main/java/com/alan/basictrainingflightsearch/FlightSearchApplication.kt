package com.alan.basictrainingflightsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.alan.basictrainingflightsearch.data.AppContainer
import com.alan.basictrainingflightsearch.data.AppDataContainer
import com.alan.basictrainingflightsearch.data.UserPreferencesRepository

private const val KEYWORDS_PREFERENCES_NAME = "keywords_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = KEYWORDS_PREFERENCES_NAME
)

class FlightSearchApplication: Application() {
    lateinit var userPreferencesRepository: UserPreferencesRepository
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}