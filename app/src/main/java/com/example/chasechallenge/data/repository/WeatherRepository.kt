package com.example.chasechallenge.data.repository

import com.example.chasechallenge.data.model.WeatherResponse
import  com.example.chasechallenge.data.model.Result
import com.example.chasechallenge.data.model.WeatherForecastResponse

interface WeatherRepository {
    suspend fun getWeather(city: String): Result<WeatherResponse>
    suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherForecastResponse>
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): Result<WeatherResponse>
}