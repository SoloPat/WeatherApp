package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.datasource.Result
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class WeatherViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel() {

    private val _viewState : MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val viewState : StateFlow<ViewState> = _viewState

    private val _searchText : MutableStateFlow<String> = MutableStateFlow("")
    val searchText : StateFlow<String> = _searchText

    fun searchTextUpdate(searchText : String){
        _searchText.value = searchText
    }

    /**
     * This function should be called only after getting the necessary permissions
     */
    fun getWeatherByCurrentLocation(lat:String = "", lon:String = ""){
        Log.d("WeatherViewModel","getWeatherByCurrentLocation Lat ${lat} Lon${lon}")
        //Get Location and call this function
        getWeather(lat, lon, "")
    }

    fun getWeather(lat: String = "",lon:String = "", city : String){
        viewModelScope.launch{
            _viewState.update { it.copy(isLoading = true) }
            val result : Result<Weather> = repository.getWeather(lat=lat, lon = lon, city = city)
            when(result){
                is Result.Success -> {
                    println("GEt weather onSuccess ${result.data}")
                    _viewState.value=  _viewState.value.copy(weather = result.data, isLoading = false, error = Result.Error("",""))
                }
                is Result.Error -> {
                    println("GEt weather onFailure")
                    //_viewState.update { it.copy(error = result.message?:"Error when loading Weather", isLoading = false) }
                    _viewState.update { it.copy(error = result, isLoading = false) }
                }
                else -> {}
            }
        }

    }


    /*fun getLatLongFromCityName(cityName: String): Pair<Double, Double>? {
        val geocoder = Geocoder(application.applicationContext)
        try {
            val addresses = geocoder.getFromLocationName(cityName, 1)
            if (addresses.isNotEmpty()) {
                val latitude = addresses[0].latitude
                val longitude = addresses[0].longitude
                return Pair(latitude, longitude)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }*/
}

data class ViewState(val weather:Weather? = null,
                     val isLoading : Boolean = false,
                     val error:Result.Error = Result.Error("",""))


