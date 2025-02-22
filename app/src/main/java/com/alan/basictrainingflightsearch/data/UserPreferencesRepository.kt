package com.alan.basictrainingflightsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    val keywords: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SEARCH_KEYWORDS] ?: ""
        }

    suspend fun saveKeywordsPreferences(keywords: String) {
        dataStore.edit { preferences ->
            preferences[SEARCH_KEYWORDS] = keywords
        }
    }

    private companion object {
        val SEARCH_KEYWORDS = stringPreferencesKey(name = "search_keywords")
        const val TAG: String = "UserPreferenceRepo"
    }
}