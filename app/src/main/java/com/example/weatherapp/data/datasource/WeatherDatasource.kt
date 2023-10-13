package com.example.weatherapp.data.datasource

import com.example.weatherapp.data.model.Weather
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherDatasource @Inject constructor(private val weatherService: WeatherService) {

    suspend fun getWeather(city:String, lat : String, lon:String, ): Result<Weather> {
        val result:Result<Weather> = safeApiCall {
                weatherService.getWeather(
                    apiKey = "d2cafe90561172c46b817955bc2d1396",//Not a good practice to hardcode api key.
                    city=city,
                    lat = lat,
                    lon = lon
                )
        }
        return result
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val cod :String,val cause: Exception? = null) : Result<Nothing>()
}

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            return Result.Success(response.body()!!)
        }
        val errorBody = response.errorBody()?.string()
        val message = errorBody ?: "Unknown error"
        return Gson().fromJson(message,Result.Error::class.java)
    } catch (e: Exception) {
        return Result.Error(e.message ?: "Unknown error", "",e)
    }
}