package com.example.parentapp.database

import androidx.room.*
import com.example.parentapp.model.CityEntity

@Dao
interface CityDao {

    @Insert
    suspend fun insertNewCity(cityEntity: CityEntity): Long

    @Query("SELECT * FROM city_table")
    fun getAllCities(): List<CityEntity>

    @Query("SELECT COUNT(id) FROM city_table")
    fun getCitiesCount(): Int

    @Query("SELECT * FROM city_table WHERE city_name = :cityName")
    fun searchForCityName(cityName: String): List<CityEntity>

    @Query("DELETE FROM city_table WHERE id = :id")
    fun deleteCity(id: Int)
}