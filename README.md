# WeatherApp
This is a weather app where a user can search for weather details of a city in the USA. The data is being fetched from api.openweathermap.org
# How to use?
1. When app is opened, user will be prompted with a location permission.
2. If user grants location permission, weather detail for the current location will be shown.
3. If the user did not grant the permission, weather for last searched city will be displayed. For the first time this may be empty.
4. User can enter the city name without the state name and get the weather details. 
5. Basic weather details are being displayed.

# Tech stack
1. This app uses Kotlin, Jetpack compose, Hilt, State Flow, Jetpack Data Store, Retrofit, Coroutines
2. MVVM is the architecture pattern used along with Repository and the data source
3. Repository looks like a pass through, but its created to highlight the need of repository and the android architecture guidelines
4. Retrofit is used for making network calls.
5. Coil is used to load image asynchronously