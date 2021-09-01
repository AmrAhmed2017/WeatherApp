package com.example.parentapp.mapper

import com.example.parentapp.model.DailyWeather
import com.example.parentapp.model.WeatherEntity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.truncate

object FutureWeatherMapper {
    fun map(item: DailyWeather, cityId: Long) = WeatherEntity(
        cityId = cityId,
        timestamp = item.dt,
        minTemp = convertTempToCelsius(item.temp.min),
        maxTemp = convertTempToCelsius(item.temp.max),
        pressure = item.pressure,
        humidity = item.humidity,
        windSpeed = item.wind_speed,
        weatherDescription = item.weather[0].description
    )

    fun selfMap(weatherEntity: WeatherEntity) = WeatherEntity(
        cityId = weatherEntity.cityId,
        timestamp = convertTimestampToDate(weatherEntity.timestamp),
        minTemp = weatherEntity.minTemp,
        maxTemp = weatherEntity.maxTemp,
        pressure = weatherEntity.pressure,
        humidity = weatherEntity.humidity,
        windSpeed = weatherEntity.windSpeed,
        weatherDescription = weatherEntity.weatherDescription
    )

    private fun convertTempToCelsius(temp: String) = truncate(temp.toDouble() - 273.15).toInt().toString()

    private fun convertTimestampToDate(temp: String): String{
        val sdf = SimpleDateFormat("EEE dd/MM/yyyy")
        val date = Date(temp.toLong() * 1000)
        return sdf.format(date)
    }
}