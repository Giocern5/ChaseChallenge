package com.example.chasechallenge.di

import android.content.Context
import com.example.chasechallenge.data.repository.WeatherRepository
import com.example.chasechallenge.data.repository.WeatherRepositoryImpl
import com.example.chasechallenge.network.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.openweathermap.org/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideRetrofit() : WeatherService {
        val instance = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return instance.create(WeatherService::class.java)
    }

    @Provides
    fun provideWeatherRepository(service: WeatherService): WeatherRepository {
        return WeatherRepositoryImpl(service)
    }
}