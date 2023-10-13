package com.example.weatherapp.di

import com.example.weatherapp.data.datasource.WeatherDatasource
import com.example.weatherapp.data.datasource.WeatherService
import com.example.weatherapp.data.repository.WeatherRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WeatherDIModule {

    @Provides
    @Singleton
    fun provideHttpClient(
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }.build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gson = GsonBuilder().create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideNetworkService(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): WeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataSource(apiService: WeatherService): WeatherDatasource {
        return WeatherDatasource(apiService)
    }

    @Provides
    @Singleton
    fun provideRepository(dataSource: WeatherDatasource): WeatherRepository {
        return WeatherRepository(dataSource)
    }
}