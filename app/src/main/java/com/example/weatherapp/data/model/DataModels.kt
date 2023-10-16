package com.example.weatherapp.data.model

import java.math.RoundingMode
import java.text.DecimalFormat

data class Weather(val weather: List<WeatherDescription>, val main:Temperature, val name:String)

data class Temperature(val temp:Float=0.0f,
                       val feels_like : Float=0.0f,
                       val temp_min : Float=0.0f,
                       val temp_max : Float=0.0f,
                       val pressure : Int=0,
                       val humidity : Int=0){

    fun displayTemp() : String{
        return convert(temp)
    }
    fun displayFeelsLike():String{
        return convert(feels_like)
    }
    fun displayMin():String{
        return convert(temp_min)
    }
    fun displayMax():String{
        return convert(temp_max)
    }
    fun convert(value: Float): String {
        val fahrenheitValue = ((value - 273.15) * (9.0/5)) + 32.0
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val convertedValue = df.format(fahrenheitValue)
        return "$convertedValue °F"
    }
}

data class WeatherDescription(val id: Int = 0, val main:String="", val description : String="", val icon:String="04d"){}

