package com.example.parentapp

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.parentapp.database.CityDao
import com.example.parentapp.database.WeatherDatabase
import com.example.parentapp.model.CityEntity
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.io.IOException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var cityDao: CityDao
    private lateinit var db: WeatherDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(
            context, WeatherDatabase::class.java).build()
        cityDao = db.cityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.parentapp", appContext.packageName)
    }

    @Test
    @Throws(Exception::class)
    fun writeCityAndReadInList() {
        val cityEntity = CityEntity(cityName = "cairo", latitude = "30", longitude = "31")
        cityDao.insertNewCity(cityEntity)
        val cities = cityDao.getAllCities()
        assertEquals(cities[0].cityName, cityEntity.cityName)
//        assertEquals(cities[0].latitude, cityEntity.latitude)
//        assertEquals(cities[0].longitude, cityEntity.longitude)
    }
}