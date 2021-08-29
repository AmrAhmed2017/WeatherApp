package com.example.parentapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.WeatherEntity

@Dao
interface CityDao {

    @Query("SELECT * FROM city_table")
    fun getAllCities(): List<CityEntity>
//
//    @Query("SELECT * FROM city_table WHERE lat = :lat AND long = :long")
//    fun getCityByCoordinates(lat: String, long: String): CityEntity
//
    @Insert
    fun insertNewCity(cityEntity: CityEntity): Long

//    @Query("DELETE FROM city_table WHERE lat = :lat AND long = :long")
//    fun deleteCity(lat: String, long: String)
}