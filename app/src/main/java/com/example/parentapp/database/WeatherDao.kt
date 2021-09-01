package com.example.parentapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.parentapp.model.WeatherEntity

@Dao
interface WeatherDao {

    @Insert
    suspend fun insertNewWeather(weatherEntities: List<WeatherEntity>)

    @Query("DELETE FROM weather_table WHERE city_id = :cityId")
    fun deleteWeather(cityId: Int)

    @Query("SELECT * FROM weather_table WHERE city_id = :cityId")
    fun selectWeathers(cityId: Int): List<WeatherEntity>

    @Query("SELECT * FROM weather_table WHERE city_id = :cityId ORDER BY timestamp ASC limit 1")
    fun selectWeather(cityId: Int): WeatherEntity
}