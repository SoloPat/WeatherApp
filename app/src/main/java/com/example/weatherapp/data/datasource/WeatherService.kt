package com.example.weatherapp.data.datasource

import com.example.weatherapp.data.model.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
//https://api.openweathermap.org/data/2.5/weather?lat=41.8967625&lon=-72.7922537&appid=d2cafe90561172c46b817955bc2d1396
    @GET("data/2.5/weather")
    suspend fun getWeather(@Query("appid") apiKey : String,
                           @Query("q") city : String,
                           @Query("lat") lat : String,
                           @Query("lon")lon : String): Response<Weather>
}