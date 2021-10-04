package com.example.weatherapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.model.CityEntity

@Dao
interface CityDao {

    @Insert
    suspend fun insertNewCity(cityEntity: CityEntity): Long

    @Query("SELECT * FROM city_table")
    fun getAllCities(): LiveData<List<CityEntity>>

    @Query("SELECT * FROM city_table WHERE id = :id")
    suspend fun getCityByItsId(id: Int): CityEntity

    @Query("SELECT COUNT(id) FROM city_table")
    suspend fun getCitiesCount(): Int

    @Query("SELECT * FROM city_table WHERE city_name = :cityName")
    suspend fun searchForCityName(cityName: String): List<CityEntity>

    @Query("DELETE FROM city_table WHERE id = :id")
    suspend fun deleteCity(id: Int)

    @Query("SELECT * FROM city_table ORDER BY id ASC limit 1")
    suspend fun selectFirstCity(): CityEntity
}