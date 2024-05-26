package com.example.chasechallenge.data.model

data class Weather(
    val id: String,
    val main: String,
    val description: String,
    val icon: String,
)

data class Wind(
    val speed: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
data class Coordinates(
    val lat: Double,
    val lon: Double
)

data class WeatherResponse(
    val id: String,
    val name: String,
    val visibility: String,
    val wind: Wind,
    val weather: List<Weather>,
    val sys: Sys,
    val main: Main,
    val coord: Coordinates,
    val dt_txt: String?
)


data class WeatherForecastResponse(
    val list: List<WeatherResponse>
)
