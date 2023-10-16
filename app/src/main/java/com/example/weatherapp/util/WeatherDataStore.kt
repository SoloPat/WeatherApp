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
        prefs[LAST_LOCATION_SEARCH]?: ""
    }
}

