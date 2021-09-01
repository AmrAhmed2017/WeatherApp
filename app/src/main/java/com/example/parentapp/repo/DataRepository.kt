package com.example.parentapp.repo

import com.example.parentapp.retrofit.RetrofitClient

object DataRepository{

    suspend fun getWeatherInfoByCityName(cityName: String = "london") =
        RetrofitClient().getRetrofitClient().getWeatherResponseByCityName(cityName)

    suspend fun getNextWeatherInfo(lat: String, long: String) =
        RetrofitClient().getRetrofitClient().getWeatherInfoForFiveDays(lat, long)
}
