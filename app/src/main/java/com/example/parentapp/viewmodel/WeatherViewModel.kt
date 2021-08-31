package com.example.parentapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentapp.database.WeatherDatabase
import com.example.parentapp.mapper.FutureWeatherMapper
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.PopulateObject
import com.example.parentapp.model.WeatherEntity
import com.example.parentapp.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherViewModel : ViewModel() {

    private val repo = DataRepository
    private lateinit var latitude: String
    private lateinit var longitude: String
    private val db = WeatherDatabase.getDatabase()
    var weatherLiveData = MutableLiveData<List<PopulateObject>>()
    var forecastLiveData = MutableLiveData<List<WeatherEntity>>()
    var countMessage = MutableLiveData<String>()

    init {
        weatherLiveData.setValue(getDataFromDatabase())
    }

    fun fetchDataFromAPIByCityName(cityName: String) {

        viewModelScope.launch(Dispatchers.IO) {
            when {
                db.cityDao()
                    .getCitiesCount() >= 5 -> countMessage.postValue("The list contains 5 cities")
                db.cityDao().searchForCityName(cityName.lowercase())
                    .isNotEmpty() -> countMessage.postValue("The list already contains this city")
                else -> {
                    val response = repo.getWeatherInfoByCityName(cityName)
                    withContext(Dispatchers.Main) {
                        latitude = response.coord.lat
                        longitude = response.coord.lon
                    }
                    fetchFutureWeatherInfo(latitude, longitude)
                }
            }
        }
    }

    private fun fetchFutureWeatherInfo(lat: String, long: String) {

        viewModelScope.launch(Dispatchers.IO) {
            repo.getNextWeatherInfo(lat, long).apply {
                saveResponseToDatabase(this)
            }
        }
    }

    private fun saveResponseToDatabase(futureWeatherResponse: FutureWeatherResponse) {
        Log.v("====", futureWeatherResponse.timezone)

        viewModelScope.launch(Dispatchers.IO) {

            val cityId = db.cityDao().insertNewCity(
                CityEntity(cityName = futureWeatherResponse.timezone.substringAfter('/').lowercase(), latitude = futureWeatherResponse.lat,
                    longitude = futureWeatherResponse.lon)
            )
            val weatherEntities = futureWeatherResponse.daily.subList(0, 5).map {
                return@map FutureWeatherMapper.map(it, cityId)
            }
            db.weatherDao().insertNewWeather(weatherEntities)
            weatherLiveData.postValue(getDataFromDatabase())
        }
    }

    private fun getDataFromDatabase(): List<PopulateObject> {
        val citiesForecast = ArrayList<PopulateObject>()
        viewModelScope.launch(Dispatchers.IO) {
            val cities = db.cityDao().getAllCities()
            cities.map {
                val currentWeather = db.weatherDao().selectWeather(it.id)
                citiesForecast.add(
                    PopulateObject(
                        id = it.id, cityName = it.cityName, minTemp = currentWeather.minTemp,
                        maxTemp = currentWeather.maxTemp
                    )
                )
            }
        }
        return citiesForecast
    }

    fun deleteSwipedItem(cityId: Int){
        viewModelScope.launch(Dispatchers.IO){

            db.cityDao().deleteCity(cityId)
            db.weatherDao().deleteWeather(cityId)
            weatherLiveData.postValue(getDataFromDatabase())
        }
    }

    fun fetchDataFromAPIByCityNameAtDefaultMode(cityName: String = "london") {

        viewModelScope.launch(Dispatchers.IO) {
            if (db.cityDao().getCitiesCount() == 0) {

                val response = repo.getWeatherInfoByCityName(cityName)
                withContext(Dispatchers.Main) {
                    latitude = response.coord.lat
                    longitude = response.coord.lon
                }
                fetchFutureWeatherInfo(latitude, longitude)

            }
        }
    }

    fun fetchFutureWeatherInfoAtDefaultMode(lat: String, long: String) {

        viewModelScope.launch(Dispatchers.IO) {
            if (db.cityDao().getCitiesCount() == 0) {
                val response = repo.getNextWeatherInfo(lat, long)
                saveResponseToDatabase(response)
            }
        }
    }

    fun getWeatherForecast(cityId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = db.weatherDao().selectWeathers(cityId)
            forecastLiveData.postValue(weather)
        }
    }
}