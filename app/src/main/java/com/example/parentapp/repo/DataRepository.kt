package com.example.parentapp.repo

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.parentapp.database.WeatherDatabase
import com.example.parentapp.mapper.FutureWeatherMapper
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.PopulateObject
import com.example.parentapp.model.WeatherEntity
import com.example.parentapp.retrofit.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DataRepository{

    suspend fun getWeatherInfoByCityName(cityName: String = "london") =
        RetrofitClient().getRetrofitClient().getWeatherResponseByCityName(cityName)

    suspend fun getNextWeatherInfo(lat: String, long: String) =
        RetrofitClient().getRetrofitClient().getWeatherInfoForFiveDays(lat, long)
}
