package com.example.parentapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.WeatherEntity

@Dao
interface CityDao {

    @Insert
    fun insertNewCity(cityEntity: CityEntity): Long

    @Query("SELECT * FROM city_table")
    fun getAllCities(): List<CityEntity>

    @Query("SELECT COUNT(id) FROM city_table")
    fun getCitiesCount(): Int

    @Query("SELECT * FROM city_table WHERE city_name = :cityName")
    fun searchForCityName(cityName: String): List<CityEntity>

    @Query("DELETE FROM city_table WHERE id = :id")
    fun deleteCity(id: Int)

//    @Query("SELECT * FROM city_table WHERE lat = :lat AND long = :long")
//    fun getCityByCoordinates(lat: String, long: String): CityEntity
//

}