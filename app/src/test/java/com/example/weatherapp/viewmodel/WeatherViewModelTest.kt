package com.example.weatherapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.weatherapp.data.model.Weather
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.invoke
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

//Todo attempt to write unit test. This is not a complete task. Given more time I would have got it working.
class WeatherViewModelTest  {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    @Mock
    lateinit var repository: WeatherRepository

    @Mock
    lateinit var observer: Observer<String>
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: WeatherViewModel

    private val testDataCity = ""
    private val testDataLat = ""
    private val testDataLon = ""
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = WeatherViewModel(repository)
        //viewModel.viewState.observeForever(observer)
    }

    //@Test
    fun fetchDataSuccess() = runTest {
        // Arrange
        //val expectedResult = Result<Weather>()
        //Mockito.`when`(repository.getWeather(city = testDataCity)).thenReturn(expectedResult)

        // Act
        viewModel.getWeather(city = testDataCity)
        testDispatcher

        // Assert
        //Mockito.verify(observer).onChanged(expectedResult)
    }

    //@Test
    fun fetchDataError() = testDispatcher.run {
        // Arrange
        val error = Exception("city not found")
        //Mockito.`when`(repository.getWeather("")).thenThrow(error)

        // Act
        viewModel.getWeather(city="")

        // Assert
        Mockito.verify(observer, Mockito.never()).onChanged(Mockito.any())
    }
}