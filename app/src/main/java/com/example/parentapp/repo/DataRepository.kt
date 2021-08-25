package com.example.parentapp.repo

import com.example.parentapp.retrofit.RetrofitClient

object DataRepository{

    suspend fun getWeatherInfoByCityName() =
        RetrofitClient().getRetrofitClient().getWeatherResponseByCityName("london")

    suspend fun getWeatherInfoByCoordinates() =
        RetrofitClient().getRetrofitClient().getWeatherResponseByCoordinates(30, 31)

    suspend fun getNextWeatherInfo() =
        RetrofitClient().getRetrofitClient().getWeatherInfoForFiveDays(30, 31)
}
