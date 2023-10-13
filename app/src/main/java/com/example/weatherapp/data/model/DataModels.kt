package com.example.weatherapp.data.model

data class Weather(val weather: List<WeatherDescription>, val main:Temperature, val name:String)

data class Temperature(val temp:Float=0.0f,
                       val feels_like : Float=0.0f,
                       val temp_min : Float=0.0f,
                       val temp_max : Float=0.0f,
                       val pressure : Int=0,
                       val humidity : Int=0)

data class WeatherDescription(val id: Int = 0, val main:String="", val description : String="", val icon:String="04d")

data class LatLong(val lat:Double=0.0, val lon: Double=0.0)