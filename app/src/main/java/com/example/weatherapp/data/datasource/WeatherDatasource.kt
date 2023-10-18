package com.example.weatherapp.data.datasource

import com.example.weatherapp.data.model.Weather
import com.google.gson.Gson
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class uses Retrofit to get data from the weather api from network
 */
@Singleton
class WeatherDatasource @Inject constructor(private val weatherService: WeatherService) {

    /**
     * This function returns weather data using city, lat and lon. If city is not valid the api
     * returns an error. Both lat and lon should be present to get a successful result.
     * //Todo Check if both lat and lon are not empty before making a network call
     */
    suspend fun getWeather(city:String, lat : String, lon:String, ): Result<Weather> {
        val result:Result<Weather> = safeApiCall {
                weatherService.getWeather(
                    apiKey = "d2cafe90561172c46b817955bc2d1396",//Todo Not a good practice to hardcode api key.
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