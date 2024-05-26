package com.example.chasechallenge.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chasechallenge.data.model.*
import com.example.chasechallenge.ui.CircularProgressBar
import com.example.chasechallenge.ui.CustomMessage
import com.example.chasechallenge.ui.viewmodel.WeatherViewModel
import com.example.chasechallenge.utils.Utils

@Composable
fun WeatherSearchScreen() {

    val viewModel =  hiltViewModel<WeatherViewModel>()
    val weatherData = viewModel.weatherData.observeAsState(initial = null)
    val weatherForecast = viewModel.hourlyWeather.observeAsState(initial = null)
    val isLoading = viewModel.isLoading.observeAsState(initial = false)
    val errorMessage = viewModel.errorMessage.observeAsState(initial = "")
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
    val savedSearch = sharedPreferences.getString("savedSearch", "")
    val latitude = sharedPreferences.getString("lat", "")
    val longitude = sharedPreferences.getString("lon", "")

    // to avoid chaining, making second api call after first call
    LaunchedEffect(key1 = weatherData.value) {
        // saving name of last valid city
        sharedPreferences.edit {
            putString("savedSearch", weatherData.value?.name)
        }
        weatherData.value?.coord?.let { coord ->
            viewModel.fetchWeatherForecast(coord.lat, coord.lon)
        }
    }

    // Showing error message
    LaunchedEffect(key1 = errorMessage.value) {
        // Clearing message after so it can pick up new errors
        if(errorMessage.value.isNotEmpty()) {
            Toast.makeText(context, errorMessage.value, Toast.LENGTH_SHORT).show()
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(Unit) {
        // if given location access, have that be default
        if(!longitude.isNullOrEmpty() && !latitude.isNullOrEmpty()) {
            viewModel.fetchWeatherByCoords(latitude.toDouble() ,longitude.toDouble())
        } // if no location access, use last saved search
         else if (!savedSearch.isNullOrEmpty()) {
             viewModel.fetchWeatherData(savedSearch)
        }
    }

    // Screen set up
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(viewModel =  viewModel)
        when {
            isLoading.value -> {
                CircularProgressBar()
            }
            weatherData.value != null -> {
                weatherData.value?.let { weather ->
                    WeatherCard(weatherData = weather, weatherForecastResponse = weatherForecast.value)
                }
            }
            weatherData.value == null && !isLoading.value -> {
                CustomMessage("Please enter a city!")
            }
        }
    }
}

@Composable
fun SearchBar(viewModel: WeatherViewModel) {
    var searchBarText by remember { mutableStateOf("") }

    Row(horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)) {

        OutlinedTextField(value = searchBarText,
            onValueChange = { text -> searchBarText = text },
            modifier = Modifier
                .height(50.dp)
                .weight(1f)
                .background(Color.Gray))
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            if(searchBarText.isNotEmpty() ) {
                viewModel.fetchWeatherData(searchBarText)
            }
        },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
            modifier = Modifier.height(50.dp)) {
            Text(text = "Search", color = Color.White)
        }
    }
}

@Composable
fun WeatherCard(weatherData: WeatherResponse, weatherForecastResponse: WeatherForecastResponse?) {
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Gray)
    ) {

        HeaderSection(name = weatherData.name, main = weatherData.main, weather = weatherData.weather)
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center 
        ) {
            Text(
                text = "Upcoming Weather Forecast",
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
        
        weatherForecastResponse?.let {
            WeatherForecastSection(weatherForecast = it)
        }
    }
}

@Composable
fun WeatherForecastSection(weatherForecast: WeatherForecastResponse) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        LazyColumn(modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(top = 3.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ){
            items(weatherForecast.list.size) { index ->
                weatherForecast.list[index].let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Gray)
                            .padding(start = 8.dp, end = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Utils.formatDateTime(it.dt_txt),
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = it.weather[0].main,
                            color = Color.White,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                        )
                        // small hack: could be more weather results per docs, so just grabbed first
                        WeatherIcon(icon = it.weather[0].icon)
                        HighLowLabel(main = it.main)
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(name: String, main: Main, weather: List<Weather>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = name,
            color = Color.White,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
        Text(
            text = "${Utils.kelvinToFahrenheit(main.temp)}°",
            color = Color.White,
            fontSize = 65.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        WeatherCell(weather = weather)
        AverageTempsCell(main = main)
    }
}

@Composable
fun WeatherCell(weather: List<Weather>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        weather.forEach {
            Text(
                text = it.main,
                color = Color.White,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
            WeatherIcon(icon = it.icon)
        }
    }
}

@Composable
fun AverageTempsCell(main: Main) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HighLowLabel(main = main)
    }
}

@Composable
fun HighLowLabel(main: Main) {
    Text(
        text = "H:${Utils.kelvinToFahrenheit(main.temp_max)}°",
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
    )
    Spacer(modifier = Modifier.width(5.dp))
    Text(
        text = "L:${Utils.kelvinToFahrenheit(main.temp_min)}°",
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun WeatherIcon(icon: String) {
    // image url is same for icons and is not in api response so hardcoding it
    AsyncImage(
        model = "https://openweathermap.org/img/wn/$icon.png",
        contentDescription = "Weather icon for $icon",
        modifier = Modifier.size(35.dp)
    )
}
