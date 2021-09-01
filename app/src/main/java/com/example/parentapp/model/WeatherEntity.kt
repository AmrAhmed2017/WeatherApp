package com.example.parentapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "city_id") var cityId: Long,
    @ColumnInfo(name = "timestamp") var timestamp: String,
    @ColumnInfo(name = "min") var minTemp: String,
    @ColumnInfo(name = "max") var maxTemp: String,
    @ColumnInfo(name = "pressure") var pressure: String,
    @ColumnInfo(name = "humidity") var humidity: String,
    @ColumnInfo(name = "wind_speed") var windSpeed: String,
    @ColumnInfo(name = "description") var weatherDescription: String
)