package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.data.datasource.WeatherDatasource
import com.example.weatherapp.data.datasource.WeatherService
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.util.WeatherDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideNetworkService(
        okHttpClient: OkHttpClient
    ): WeatherService {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
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
    fun provideRepository(dataSource: WeatherDatasource, dataStore: WeatherDataStore): WeatherRepository {
        return WeatherRepository(dataSource, dataStore)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): WeatherDataStore {
        return WeatherDataStore(context)
    }
}