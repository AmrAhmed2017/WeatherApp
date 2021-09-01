package com.example.parentapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.parentapp.model.WeatherEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: WeatherDatabase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), WeatherDatabase::class.java).allowMainThreadQueries().build()
        weatherDao = db.weatherDao()
    }

    @After
    fun teardown() {
        db.close()
    }


    @Test
    fun insertNewWeather_andCheckExist() = runBlockingTest {

        val weatherEntity = WeatherEntity(id = 1, cityId = 1, timestamp = "123456", minTemp = "12", maxTemp = "24")
        weatherDao.insertNewWeather(listOf(weatherEntity))
        val weather = weatherDao.selectWeathers(1)
        assertThat(weather).contains(weatherEntity)
    }
}