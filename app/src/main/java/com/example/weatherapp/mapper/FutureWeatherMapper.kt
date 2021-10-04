package com.example.weatherapp.mapper

import android.annotation.SuppressLint
import com.example.weatherapp.model.DailyWeather
import com.example.weatherapp.model.Temperature
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.truncate

object FutureWeatherMapper {

    fun map(item: DailyWeather) = DailyWeather(
        dt = convertTimestampToDate(item.dt),
        temp = Temperature(min = convertTempToCelsius(item.temp.min), max = convertTempToCelsius(item.temp.max)),
        pressure = item.pressure,
        humidity = item.humidity,
        wind_speed = item.wind_speed,
        weather = item.weather
    )

    private fun convertTempToCelsius(temp: String) = truncate(temp.toDouble() - 273.15).toInt().toString()

    @SuppressLint("SimpleDateFormat")
    private fun convertTimestampToDate(temp: String): String{
        val sdf = SimpleDateFormat("EEE dd/MM/yyyy")
        val date = Date(temp.toLong() * 1000)
        return sdf.format(date)
    }
}