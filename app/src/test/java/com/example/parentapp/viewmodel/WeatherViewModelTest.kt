package com.example.parentapp.viewmodel

import com.example.parentapp.model.WeatherResponse
import com.example.parentapp.repo.DataRepository
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherViewModelTest{
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun before() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun after() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun testYourFunc() = testScope.runBlockingTest {
        val mockRepo = mock<DataRepository> {
            onBlocking { getWeatherInfoByCityName() }
        }

        val viewModel = WeatherViewModel()

        val result = viewModel.fetchDataFromAPIByCityName() // Or however you can retrieve actual changes the repo made to viewmodel

        // assert your case
    }
}