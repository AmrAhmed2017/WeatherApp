package com.example.weatherapp.database

import androidx.room.TypeConverter
import com.example.weatherapp.model.DailyWeather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverter{
    companion object{

        @JvmStatic
        @TypeConverter
        fun fromString(value: String): List<DailyWeather> {
            val listType = object : TypeToken<List<DailyWeather>>() {

            }.type
            return Gson().fromJson(value, listType)
        }

        @JvmStatic
        @TypeConverter
        fun fromList(list: List<DailyWeather>): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}