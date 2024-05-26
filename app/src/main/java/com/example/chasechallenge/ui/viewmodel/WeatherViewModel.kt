package com.example.chasechallenge.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chasechallenge.data.model.WeatherResponse
import com.example.chasechallenge.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import  com.example.chasechallenge.data.model.Result
import com.example.chasechallenge.data.model.WeatherForecastResponse

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    companion object {
        const val TAG = "WeatherViewModel"
    }

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> get() = _weatherData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _hourlyWeather = MutableLiveData<WeatherForecastResponse>()
    val hourlyWeather:LiveData<WeatherForecastResponse> get() = _hourlyWeather
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchWeatherData(city: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.getWeather(city)) {
                is Result.Success -> {
                    _weatherData.postValue(result.data)
                    _isLoading.postValue( false)
                }
                is Result.Error -> {
                    Log.e(TAG, result.exception.message.toString())
                    _errorMessage.postValue(result.exception.message.toString())
                    _isLoading.postValue( false)
                }
            }
        }
    }

    fun fetchWeatherForecast(lat: Double, lon: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.getWeatherForecast(lat,lon)) {
                is Result.Success -> {
                    _hourlyWeather.postValue(result.data)
                }
                is Result.Error -> {
                    Log.e(TAG, result.exception.message.toString())
                }
            }
        }
    }

    // adding by coords since device location only gives coords
    // didnt want to use Geocoder api to make an api call to convert the city from coords
    // so used a separate api to take in coords so it can be 1 request instead of 2
    fun fetchWeatherByCoords(lat: Double, lon: Double) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.getWeatherByCoordinates(lat,lon)) {
                is Result.Success -> {
                    _weatherData.postValue(result.data)
                    _isLoading.postValue( false)
                }
                is Result.Error -> {
                    Log.e(TAG, result.exception.message.toString())
                    _isLoading.postValue( false)
                }
            }
        }
    }

    fun clearErrorMessage() {
        _errorMessage.postValue("")
    }
}
