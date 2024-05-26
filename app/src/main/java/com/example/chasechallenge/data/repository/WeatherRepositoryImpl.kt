package com.example.chasechallenge.data.repository

import com.example.chasechallenge.data.model.WeatherResponse
import com.example.chasechallenge.network.WeatherService
import javax.inject.Inject
import  com.example.chasechallenge.data.model.Result
import com.example.chasechallenge.data.model.WeatherForecastResponse
import java.io.IOException

// due to time, would store key somewhere safer like build.gradle file
private const val apiKey = "2543e0ffbcd4b196e8460afac39307ed"
private const val LIMIT = 40

class WeatherRepositoryImpl @Inject constructor(private val weatherService: WeatherService)
    : WeatherRepository {

    override suspend fun getWeather(city: String): Result<WeatherResponse> {
        return try {
            val response = weatherService.getWeather(city, apiKey)
            if (response.isSuccessful) {
                val info = response.body()
                if(info != null) {
                    Result.Success(info)
                } else {
                    Result.Error(Exception("Empty Response"))
                }

            } else {
                Result.Error(Exception("Enter a valid city!"))
            }
        } catch (e: IOException) {
            Result.Error(e)
        }
    }

    override suspend fun getWeatherForecast(lat: Double, lon: Double): Result<WeatherForecastResponse> {
        return try {
            val response = weatherService.getWeatherForecast(lat, lon, LIMIT, apiKey)
            if (response.isSuccessful) {
                val info = response.body()
                if(info != null) {
                    Result.Success(info)
                } else {
                    Result.Error(Exception("Empty Response"))
                }
            } else {
                Result.Error(Exception("Enter a valid city!!"))
            }
        } catch (e: IOException) {
            Result.Error(e)
        }

    }

    override suspend fun getWeatherByCoordinates(
        lat: Double,
        lon: Double
    ): Result<WeatherResponse> {
        return try {
            val response = weatherService.getWeatherByCoordinates(lat, lon, apiKey)
            if (response.isSuccessful) {
                val info = response.body()
                if(info != null) {
                    Result.Success(info)
                } else {
                    Result.Error(Exception("Empty Response"))
                }
            } else {
                Result.Error(Exception("Enter valid coordinates!!"))
            }
        } catch (e: IOException) {
            Result.Error(e)
        }
    }
}