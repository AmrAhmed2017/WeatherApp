package com.example.parentapp.retrofit

import com.example.parentapp.BuildConfig
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/data/2.5/weather")
    suspend fun getWeatherResponseByCityName(@Query("q") cityName: String, @Query("appid") appId: String = BuildConfig.APP_ID): WeatherResponse

    @GET("/data/2.5/weather")
    suspend fun getWeatherResponseByCoordinates(@Query("lat") latitude: Int, @Query("lon") longitude: Int,
                                                @Query("appid") appId: String = BuildConfig.APP_ID): WeatherResponse

    @GET("/data/2.5/onecall")
    suspend fun getWeatherInfoForFiveDays(@Query("lat") latitude: Int, @Query("lon") longitude: Int,
                                          @Query("exclude") exclude: String = "hourly,minutely", @Query("appid") appId: String = BuildConfig.APP_ID): FutureWeatherResponse
}