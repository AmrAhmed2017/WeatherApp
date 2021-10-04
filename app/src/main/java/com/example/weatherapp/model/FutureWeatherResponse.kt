package com.example.weatherapp.model

data class FutureWeatherResponse(
    val lat: String,
    val lon: String,
    val timezone: String,
    val daily: List<DailyWeather>
)
