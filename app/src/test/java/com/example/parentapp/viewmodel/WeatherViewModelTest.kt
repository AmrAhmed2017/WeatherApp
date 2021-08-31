package com.example.parentapp.viewmodel

import com.example.parentapp.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class WeatherViewModelTest{

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
//    private lateinit var mockRepo: mock<DataRepository>
    private lateinit var viewModel: WeatherViewModel

//    @Before
//    fun before() {
//        Dispatchers.setMain(testDispatcher)
//        mockRepo = mock<DataRepository>()
//        viewModel = WeatherViewModel()
//    }
//
//    @After
//    fun after() {
//        Dispatchers.resetMain()
//        testScope.cleanupTestCoroutines()
//    }

//    @Test
//    fun testYourFunc() = testScope.runBlockingTest {
//
//
//
//
//        val result = viewModel.fetchDataFromAPIByCityName() // Or however you can retrieve actual changes the repo made to viewmodel
//
//        // assert your case
//    }
}