package com.example.weatherapp.retrofit

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.model.FutureWeatherResponse
import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/data/2.5/weather")
    suspend fun getWeatherResponseByCityName(@Query("q") cityName: String, @Query("appid") appId: String = BuildConfig.APP_ID): Response<WeatherResponse>

    @GET("/data/2.5/onecall")
    suspend fun getWeatherInfoForFiveDays(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("exclude") exclude: String = "hourly,minutely",
        @Query("appid") appId: String = BuildConfig.APP_ID)
    : Response<FutureWeatherResponse>
}