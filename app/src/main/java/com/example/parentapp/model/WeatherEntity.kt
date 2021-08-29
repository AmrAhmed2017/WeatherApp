package com.example.parentapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherEntity(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "city_id") var cityId: Int,
    @ColumnInfo(name = "timestamp") var timestamp: String,
    @ColumnInfo(name = "min") var minTemp: String,
    @ColumnInfo(name = "max") var maxTemp: String,
)
//{
//    constructor(cityId: Int, timestamp: String, minTemp: String, maxTemp: String) : this(Int.MIN_VALUE, cityId, timestamp, minTemp, maxTemp)
//}