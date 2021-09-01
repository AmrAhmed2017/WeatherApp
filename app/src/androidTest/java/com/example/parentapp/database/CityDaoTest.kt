package com.example.parentapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.parentapp.model.CityEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class CityDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: WeatherDatabase
    private lateinit var cityDao: CityDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(), WeatherDatabase::class.java).allowMainThreadQueries().build()
        cityDao = db.cityDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertNewCity_andCheckExist() = runBlockingTest {

        val cityEntity = CityEntity(id = 1, cityName = "cairo", latitude = "30", longitude = "31")
        cityDao.insertNewCity(cityEntity)
        val allCities = cityDao.getAllCities()
        assertThat(allCities[0].id).isEqualTo(cityEntity.id)
    }
}