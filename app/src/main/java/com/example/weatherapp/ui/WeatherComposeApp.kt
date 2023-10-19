package com.example.weatherapp.ui

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.weatherapp.data.model.Temperature
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.WeatherDescription
import com.example.weatherapp.util.PermiChecker
import com.example.weatherapp.util.WeatherDataStore
import com.example.weatherapp.util.getUserLocation
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

//Entry point to the search screen
@Composable
fun WeatherComposeApp() {
    MySearchBar()
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalComposeUiApi::class
)
/**
 * I have simplified the search bar. Initially I added a type ahead search bar which will search
 * search for city upon typing using google places API. Due time constraints, I did not continue that approach.
 */
@Composable
fun MySearchBar(){
    Log.d("MySearchBar","MySearchBarCalled")
    val weatherViewModel: WeatherViewModel = hiltViewModel()
    val viewState by weatherViewModel.viewState.collectAsState()

    val searchText by  weatherViewModel.searchText.collectAsState()
    val context = LocalContext.current
    val dataStore = remember {
        WeatherDataStore(context)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val lastSearch = dataStore.lastLocationFlow.collectAsState("")
    Log.d("MySearchBar","Last Searched String=${lastSearch}")
    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit){
        Log.d("MySearchBar","LaunchedEffect Called")

        lastSearch.value.let {
            Log.d("MySearchBar","Updating search text from last search ${it}")
            weatherViewModel.searchTextUpdate(it)
            weatherViewModel.getWeather(city = it)
        }
        permissionsState.launchMultiplePermissionRequest()
    }

    PermiChecker(permissionsState,
        grantedContent = {
            LaunchedEffect(Unit){
                getUserLocation(context) {
                    weatherViewModel.getWeatherByCurrentLocation(lat = it.latitude, lon = it.longitude)
                }
            }
            Log.d("MySearchBar","PermiChecker GrantedContent Called")
        },
        notGrantedContent = {
            Log.d("MySearchBar","PermiChecker NOTGrantedContent Called")
            weatherViewModel.getWeather(city = lastSearch.value) //Load last searched city if location permission is not granted
        },
        notAvailableContent = {})

    Column (Modifier.padding(5.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(modifier = Modifier
                .weight(2f)
                .padding(10.dp),
                value = searchText,
                label = { Text(text = "E.g. New York")},
                onValueChange = { weatherViewModel.searchTextUpdate(it) },
                leadingIcon = { Icon( imageVector = Icons.Default.Search, contentDescription = "search icon" )})

            Button(onClick = {
                keyboardController?.hide()
                weatherViewModel.getWeather(city = searchText)
                             },
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)) {
                Text(text = "Search")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LoadingIndicator(isLoading = viewState.isLoading)
        Spacer(modifier = Modifier.height(16.dp))
        if(viewState.error.message.isNotEmpty() || viewState.weather?.name == null){
            Text(text = " Search Again!!! ${viewState.error.message}")
        }else {
            WeatherResultList(weatherState = viewState.weather)
        }
    }
}
@Composable
fun WeatherResultList(weatherState: Weather?){
    Column {
        CityName(cityName = weatherState?.name?:"")
        WeatherUI(wd = (weatherState?.weather?.get(0)?:WeatherDescription()))
        TemperatureUI(temp = weatherState?.main?: Temperature())
    }
}
@Composable
fun CityName(cityName:String){

    Text(text = cityName, modifier = Modifier
        .padding(5.dp)
        .fillMaxWidth(1f), style = TextStyle(fontSize = 24.sp), textAlign = TextAlign.Center)
}

@Composable
fun WeatherUI(wd: WeatherDescription){
    Column {
        val request: ImageRequest = ImageRequest.Builder(LocalContext.current.applicationContext)
            .data("https://openweathermap.org/img/wn/${wd.icon}@2x.png") //Todo This URL should be configurable
            .crossfade(true)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            .diskCacheKey(wd.icon)
            .build()

        AsyncImage(model = request,
            modifier = Modifier
            .clip(CircleShape)
            .height(120.dp)
            .width(120.dp)
            .fillMaxWidth(1f)
            .align(Alignment.CenterHorizontally),  contentDescription = "Weather icon")
        CenteredText(text = wd.description)
        Spacer(modifier = Modifier.height(24.dp))
    }
}
@Composable
fun CenteredText(text : String){
    Text(text = text,style = TextStyle(fontSize = 18.sp,
        color = Color.Black),
        textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth(1f)
            .padding(5.dp) )
}




@Composable
fun TemperatureUI(temp:Temperature){
    Card() {
        CenteredText(text = "Temperature")
        CenteredText("Overall Temperature:${temp.displayTemp()}")
        CenteredText("Min:${temp.displayMin()}")
        CenteredText("Max:${temp.displayMax()}")
        CenteredText("FeelsLike:${temp.displayFeelsLike()}")
    }
}

@Composable
fun LoadingIndicator(isLoading : Boolean) {
    val loading by remember { mutableStateOf(isLoading) }
    if (!loading) return
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    )

}




@Preview
@Composable
fun PreviewSearchBar(){
    MySearchBar()
}

@Preview
@Composable
fun CityNamePreview(){
    CityName(cityName = "Simsbury")
}

@Preview
@Composable
fun WeatherPreview(){
    val wd = WeatherDescription(803, main = "CLouds", description = "Broken Clouds", icon = "04d")
    WeatherUI(wd)
}
@Preview
@Composable
fun TemperaturePreview(){
    val temp = Temperature(75f, 65f, 70f, 80f,100,10)
    TemperatureUI(temp = temp)
}

