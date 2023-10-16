package com.example.weatherapp

import android.app.Application
import android.content.Context
import androidx.core.content.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApplication: Application() {
    override fun onCreate() {
        super.onCreate()

    }
}