package com.example.weatherapp.repo

import com.example.weatherapp.database.WeatherDatabase
import com.example.weatherapp.mapper.FutureWeatherMapper
import com.example.weatherapp.model.*
import com.example.weatherapp.retrofit.RetrofitClient
import com.example.weatherapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

object DataRepository{

    private val db = WeatherDatabase.getDatabase()

    suspend fun getCitiesCount() = GlobalScope.async(Dispatchers.IO) {
        db.cityDao().getCitiesCount()
    }.await()

    suspend fun getWeatherInfoByCityName(cityName: String): Resource<WeatherResponse>{

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

    suspend fun getWeatherInfoByCityCoordinates(lat: String, long: String): Resource<FutureWeatherResponse>{
        val response = RetrofitClient().getRetrofitClient().getWeatherInfoForFiveDays(lat, long)
        return if (response.isSuccessful){
            response.body()?.let {
                saveResponseToDatabase(it)
                Resource.success(it)
            } ?: Resource.error("Unknown Error", null)
        }else{
            Resource.error("Error occurred while searching for this city," +
                    " make sure that your network is connected or you typed city name in correct way", null)
        }
    }

    suspend fun searchForCityName(cityName: String) = db.cityDao().searchForCityName(cityName)

    private suspend fun insertNewCity(cityEntity: CityEntity) = db.cityDao().insertNewCity(cityEntity)

    suspend fun getCityByItsId(id: Int) = db.cityDao().getCityByItsId(id)

    fun getAllCities() = db.cityDao().getAllCities()

    suspend fun deleteCity(id: Int){
        db.cityDao().deleteCity(id)
    }

    private fun saveResponseToDatabase(futureWeatherResponse: FutureWeatherResponse) {

        GlobalScope.launch(Dispatchers.IO) {

            val weatherEntities = futureWeatherResponse.daily.subList(0, 5).map {
                return@map FutureWeatherMapper.map(it)
            }
            insertNewCity(
                CityEntity(cityName = futureWeatherResponse.timezone.substringAfter('/').lowercase(), latitude = futureWeatherResponse.lat,
                    longitude = futureWeatherResponse.lon, dailyWeather = weatherEntities))
        }
    }
}
