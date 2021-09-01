package com.example.parentapp.model

data class DailyWeather(
    val dt: String,
    val temp: Temperature,
    val pressure: String,
    val humidity: String,
    val wind_speed: String,
    val weather: List<WeatherDescription>
)
