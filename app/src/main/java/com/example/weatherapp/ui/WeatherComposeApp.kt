package com.example.weatherapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.weatherapp.data.model.Temperature
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.model.WeatherDescription
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.gson.Gson


@Composable
fun WeatherComposeApp() {
    MySearchBar()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySearchBar(weatherViewModel: WeatherViewModel = viewModel()){
    //val weatherViewModel : WeatherViewModel = viewModel()

    val viewState by weatherViewModel.viewState.collectAsState()

    val searchText by  weatherViewModel.searchText.collectAsState()

    Column (Modifier.padding(5.dp)){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(modifier = Modifier
                .weight(2f)
                .padding(10.dp), value = searchText,
                label = { Text(text = "E.g. New York, NY,USA")},
                onValueChange = { weatherViewModel.searchTextUpdate(it) },
                leadingIcon = { Icon( imageVector = Icons.Default.Search, contentDescription = "search icon" )})
            Button(onClick = {
                weatherViewModel.getWeather(searchText)
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
        if(!viewState.error.message.isNullOrEmpty() || viewState.weather?.name == null){
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
fun Modifier.heading(size: Int) = this.then(Modifier.size(size.dp))

@Composable
fun WeatherUI(wd: WeatherDescription){
    Column {
        AsyncImage(modifier = Modifier
            .clip(CircleShape)
            .height(120.dp)
            .width(120.dp)
            .fillMaxWidth(1f)
            .align(Alignment.CenterHorizontally), model="https://openweathermap.org/img/wn/${wd.icon}@2x.png", contentDescription = "Weather icon")
        CenteredText(text = "${wd.description}")
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
        CenteredText("Overall Temperature:${temp.temp}")
        CenteredText("Min:${temp.temp_min}")
        CenteredText("Max:${temp.temp_max}")
        CenteredText("FeelsLike:${temp.feels_like}")
    }
}

@Composable
fun LoadingIndicator(isLoading : Boolean) {
    val loading by remember { mutableStateOf(isLoading) }
    if (!loading) return
    CircularProgressIndicator(
        modifier = Modifier.width(64.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
        //trackColor = MaterialTheme.colorScheme.secondary,
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

private fun getWeather():Weather{

    return Gson().fromJson("{\n" +
            "  \"coord\": {\n" +
            "    \"lon\": -72.7923,\n" +
            "    \"lat\": 41.8968\n" +
            "  },\n" +
            "  \"weather\": [\n" +
            "    {\n" +
            "      \"id\": 803,\n" +
            "      \"main\": \"Clouds\",\n" +
            "      \"description\": \"broken clouds\",\n" +
            "      \"icon\": \"04d\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"base\": \"stations\",\n" +
            "  \"main\": {\n" +
            "    \"temp\": 284.34,\n" +
            "    \"feels_like\": 283.39,\n" +
            "    \"temp_min\": 282.7,\n" +
            "    \"temp_max\": 286.07,\n" +
            "    \"pressure\": 1007,\n" +
            "    \"humidity\": 72\n" +
            "  },\n" +
            "  \"visibility\": 10000,\n" +
            "  \"wind\": {\n" +
            "    \"speed\": 3.09,\n" +
            "    \"deg\": 200\n" +
            "  },\n" +
            "  \"clouds\": {\n" +
            "    \"all\": 75\n" +
            "  },\n" +
            "  \"dt\": 1696861293,\n" +
            "  \"sys\": {\n" +
            "    \"type\": 2,\n" +
            "    \"id\": 2009442,\n" +
            "    \"country\": \"US\",\n" +
            "    \"sunrise\": 1696848977,\n" +
            "    \"sunset\": 1696890025\n" +
            "  },\n" +
            "  \"timezone\": -14400,\n" +
            "  \"id\": 4831766,\n" +
            "  \"name\": \"Tariffville\",\n" +
            "  \"cod\": 200\n" +
            "}", Weather::class.java)
}

/*var active by remember { mutableStateOf(true) }
    val prevSearch = remember { mutableStateListOf("Test 124") }
        SearchBar(query = text,
            onQueryChange = {
                active = true
                text = it
                println("onQueryChange Called Text = ${it}")
                            },
            onSearch = {
                prevSearch.add(it)
                println("On Search adding element to history = ${it} Current list=${prevSearch.joinToString { "" }}")
                active = false
                       },
            active = true,
            onActiveChange = { active = it },
            placeholder = { Text(text = "Enter City")},
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon") },
            trailingIcon = {
                Icon(imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Icon",
                    modifier = Modifier.clickable {
                        if(text.isNotEmpty()){
                            text=""
                        }else{
                            active = false
                        }
                    })
            }) {
            prevSearch.forEach {
                Row(modifier = Modifier.padding(10.dp)){
                    Icon(imageVector = Icons.Default.Refresh,
                        contentDescription = "History Icon")
                    Text(text = it)
                }
            }

            WeatherResultList()
        }
*/