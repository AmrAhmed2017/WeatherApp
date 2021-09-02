package com.example.parentapp.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentapp.mapper.FutureWeatherMapper
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.CityEntity
import com.example.parentapp.model.PopulateObject
import com.example.parentapp.model.WeatherEntity
import com.example.parentapp.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repo = DataRepository
    private lateinit var latitude: String
    private lateinit var longitude: String
    var weatherLiveData = MutableLiveData<List<PopulateObject>>()
    var forecastLiveData = MutableLiveData<List<WeatherEntity>>()
    var countMessage = MutableLiveData<String>()

    init {
        weatherLiveData.value = getDataFromDatabase()
    }

    fun fetchDataFromAPIByCityName(cityName: String) {

        viewModelScope.launch(Dispatchers.IO) {
            when {
                repo.getCitiesCount() >= 5 -> countMessage.postValue("The list contains 5 cities")
                repo.searchForCityName(cityName.lowercase())
                    .isNotEmpty() -> countMessage.postValue("The list already contains this city")
                else -> {
                    val response = repo.getWeatherInfoByCityName(cityName)
                        response.data?.let {
                            latitude = it.coord.lat
                            longitude = it.coord.lon
                            fetchFutureWeatherInfo(latitude, longitude)
                        } ?: countMessage.postValue(response.message!!)

                }
            }
        }
    }

    private fun fetchFutureWeatherInfo(lat: String, long: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getNextWeatherInfo(lat, long)
                response.data?.let {
                saveResponseToDatabase(it)
            } ?: countMessage.postValue(response.message!!)
        }
    }

    private fun saveResponseToDatabase(futureWeatherResponse: FutureWeatherResponse) {
        Log.v("====", futureWeatherResponse.timezone)

        viewModelScope.launch(Dispatchers.IO) {

            val cityId = repo.insertNewCity(
                CityEntity(cityName = futureWeatherResponse.timezone.substringAfter('/').lowercase(), latitude = futureWeatherResponse.lat,
                    longitude = futureWeatherResponse.lon)
            )
            val weatherEntities = futureWeatherResponse.daily.subList(0, 5).map {
                return@map FutureWeatherMapper.map(it, cityId)
            }
            repo.insertNewWeather(weatherEntities)
            weatherLiveData.postValue(getDataFromDatabase())
        }
    }

    private fun getDataFromDatabase(): List<PopulateObject> {
        val citiesForecast = ArrayList<PopulateObject>()
        viewModelScope.launch(Dispatchers.IO) {
            val cities = repo.getAllCities()
            cities.map {
                val currentWeather = repo.selectWeather(it.id)
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

            repo.deleteCity(cityId)
            repo.deleteWeather(cityId)
            weatherLiveData.postValue(getDataFromDatabase())
        }
    }

    fun fetchDataFromAPIByCityNameAtDefaultMode() {

        viewModelScope.launch(Dispatchers.IO) {
            if (repo.getCitiesCount() == 0) {
                val response = repo.getWeatherInfoByCityName("london")
                    response.data?.let {
                        latitude = it.coord.lat
                        longitude = it.coord.lon
                        fetchFutureWeatherInfo(latitude, longitude)

                    } ?: countMessage.postValue(response.message!!)

            }
        }
    }

    fun fetchFutureWeatherInfoAtDefaultMode(lat: String, long: String) {

        viewModelScope.launch(Dispatchers.IO) {
            if (repo.getCitiesCount() == 0) {
                val response = repo.getNextWeatherInfo(lat, long)
                response.data?.let {
                    saveResponseToDatabase(it)
                } ?: countMessage.postValue(response.message!!)
            }
        }
    }

    fun getWeatherForecast(cityId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = repo.selectWeathers(cityId)
            forecastLiveData.postValue(weather)
        }
    }
}