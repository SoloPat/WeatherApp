package com.example.weatherapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.ui.WeatherComposeApp

@Composable
fun WeatherNav(navController: NavHostController) {
    NavHost(navController, startDestination="main_page"){
       composable("main_page"){ WeatherComposeApp() }
    }
}