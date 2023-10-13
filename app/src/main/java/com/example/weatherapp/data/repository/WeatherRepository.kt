package com.example.weatherapp.data.repository

import com.example.weatherapp.data.datasource.Result
import com.example.weatherapp.data.datasource.WeatherDatasource
import com.example.weatherapp.data.model.Weather
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val datasource: WeatherDatasource) {
    suspend fun getWeather(city : String,lat : String = "", lon : String=""):Result<Weather>{
        return datasource.getWeather(city, lat, lon)
    }
}