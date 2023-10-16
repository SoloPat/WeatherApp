package com.example.weatherapp.data.repository

import com.example.weatherapp.data.datasource.Result
import com.example.weatherapp.data.datasource.WeatherDatasource
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.util.WeatherDataStore
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val datasource: WeatherDatasource, private val dataStore: WeatherDataStore) {
    suspend fun getWeather(city : String,lat : String = "", lon : String=""):Result<Weather>{
        dataStore.saveLastSearchedLocation(city)
        return datasource.getWeather(city = city, lat=lat, lon=lon)
    }
}