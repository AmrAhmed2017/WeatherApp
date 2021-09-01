package com.example.parentapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_table")
class CityEntity(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "city_name") var cityName: String,
    @ColumnInfo(name = "lat") var latitude: String,
    @ColumnInfo(name = "long") var longitude: String,
)