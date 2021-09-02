package com.example.parentapp.repo

import com.example.parentapp.database.WeatherDatabase
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.WeatherEntity
import com.example.parentapp.model.WeatherResponse
import com.example.parentapp.retrofit.RetrofitClient
import com.example.parentapp.utils.Resource

object DataRepository{

    private val db = WeatherDatabase.getDatabase()

    suspend fun getWeatherInfoByCityName(cityName: String = "london"): Resource<WeatherResponse>{

        val response = RetrofitClient().getRetrofitClient().getWeatherResponseByCityName(cityName)
        return if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: return Resource.error("Unknown Error", null)
        }else{
                Resource.error("Error occurred while searching for this city," +
                        " make sure that your network is connected or you typed city name in correct way", null)
        }
    }

    suspend fun getNextWeatherInfo(lat: String, long: String): Resource<FutureWeatherResponse>{
        val response = RetrofitClient().getRetrofitClient().getWeatherInfoForFiveDays(lat, long)
        return if (response.isSuccessful){
            response.body()?.let {
                return@let Resource.success(it)
            } ?: return Resource.error("Unknown Error", null)
        }else{
            Resource.error("Error occurred while searching for this city," +
                    " make sure that your network is connected or you typed city name in correct way", null)
        }
    }

    suspend fun getCitiesCount() = db.cityDao().getCitiesCount()

    suspend fun searchForCityName(cityName: String) = db.cityDao().searchForCityName(cityName)

    suspend fun insertNewCity(cityEntity: CityEntity) = db.cityDao().insertNewCity(cityEntity)

    suspend fun getAllCities() = db.cityDao().getAllCities()

    suspend fun deleteCity(id: Int){
        db.cityDao().deleteCity(id)
    }

    suspend fun insertNewWeather(weatherEntities: List<WeatherEntity>){
        db.weatherDao().insertNewWeather(weatherEntities)
    }

    suspend fun selectWeather(cityId: Int) = db.weatherDao().selectWeather(cityId)

    suspend fun deleteWeather(cityId: Int){
        db.weatherDao().deleteWeather(cityId)
    }

    suspend fun selectWeathers(cityId: Int) = db.weatherDao().selectWeathers(cityId)
}
