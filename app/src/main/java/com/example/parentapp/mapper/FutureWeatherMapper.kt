package com.example.parentapp.mapper

import com.example.parentapp.model.DailyWeather
import com.example.parentapp.model.WeatherEntity

object FutureWeatherMapper {
    fun map(item: DailyWeather, cityId: Long) = WeatherEntity(
        cityId = cityId,
        timestamp = item.dt,
        minTemp = item.temp.min,
        maxTemp = item.temp.max
    )
}