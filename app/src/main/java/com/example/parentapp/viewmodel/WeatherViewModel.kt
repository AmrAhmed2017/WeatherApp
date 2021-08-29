package com.example.parentapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentapp.database.WeatherDatabase
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.PopulateObject
import com.example.parentapp.model.WeatherEntity
import com.example.parentapp.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel: ViewModel() {

    private val repo = DataRepository
    private lateinit var latitude: String
    private lateinit var longitude: String
    private val db = WeatherDatabase.getDatabase()
    var weatherLiveData = MutableLiveData<List<CityEntity>>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            weatherLiveData.postValue(db.cityDao().getAllCities())
        }
    }

    fun fetchDataFromAPIByCityName(cityName: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeatherInfoByCityName(cityName)
            withContext(Dispatchers.Main) {
                latitude = response.coord.lat
                longitude = response.coord.lon
            }
            fetchFutureWeatherInfo(latitude, longitude)

        }
    }

    fun fetchFutureWeatherInfo(lat: String, long: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getNextWeatherInfo(lat, long)
            saveResponseToDatabase(response)
        }
    }

    private fun saveResponseToDatabase(futureWeatherResponse: FutureWeatherResponse) {
        Log.v("====", futureWeatherResponse.timezone)

        viewModelScope.launch(Dispatchers.IO) {

            val cityId = db.cityDao().insertNewCity(CityEntity(cityName = futureWeatherResponse.timezone, latitude = futureWeatherResponse.lat,
                longitude = futureWeatherResponse.lon))
            for (i in 0..4){
                db.weatherDao().insertNewWeather(WeatherEntity(cityId = cityId.toInt(), timestamp = futureWeatherResponse.daily[i].dt,
                                                minTemp = futureWeatherResponse.daily[i].temp.min, maxTemp = futureWeatherResponse.daily[i].temp.max))
            }

            weatherLiveData.postValue(db.cityDao().getAllCities())
        }
    }

//    fun getDataFromDatabase(): MutableLiveData<List<PopulateObject>>{
//        val citiesForecast = ArrayList<PopulateObject>()
//        viewModelScope.launch(Dispatchers.IO) {
//            val cities = db.cityDao().getAllCities()
//            cities.forEach {
//                val currentWeather = db.weatherDao().selectWeather(it.id)
//                citiesForecast.add(PopulateObject(id = it.id, cityName = it.cityName, minTemp = currentWeather.minTemp,
//                    maxTemp = currentWeather.maxTemp))}
//
//            weatherLiveData.postValue(citiesForecast)
//            }
//        return weatherLiveData
//    }
}