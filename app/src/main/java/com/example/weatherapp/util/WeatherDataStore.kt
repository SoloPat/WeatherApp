package com.example.weatherapp.util

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * This is the data store implementation for the app. Currently this only stores the last searched city.
 * //todo If the data set is complex a room db is advisable. Since this is only a string using a simpler option
 */
class WeatherDataStore(private val context : Context) {
    companion object {
        val TAG : String ="WeatherDataStore"
        val Context.weatherDataStore: DataStore<Preferences> by preferencesDataStore(name = "WeatherDataStore")
        private val LAST_LOCATION_SEARCH = stringPreferencesKey("last_searched_location")
    }

    suspend fun saveLastSearchedLocation(lastLocation:String){
        Log.d(TAG, "saveLastSearchedLocation${lastLocation}")
        context.weatherDataStore.edit{pref ->
            pref[LAST_LOCATION_SEARCH] = lastLocation
        }
    }

    val lastLocationFlow : Flow<String> = context.weatherDataStore.data.map { prefs->
        prefs[LAST_LOCATION_SEARCH]?: "EMPTY Value"
    }
}

