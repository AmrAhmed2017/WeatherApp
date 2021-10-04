package com.example.weatherapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.model.CityEntity
//import com.example.weatherapp.model.WeatherEntity
import com.example.weatherapp.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {

    private val repo = DataRepository
    private lateinit var latitude: String
    private lateinit var longitude: String
    var warningMessage = MutableLiveData<String>()

    fun searchForCityForecastByCityName(cityName: String = "london") {

        viewModelScope.launch(Dispatchers.IO) {
            when {
                repo.getCitiesCount() >= 5 -> warningMessage.postValue("The list contains 5 cities")
                repo.searchForCityName(cityName.lowercase())
                    .isNotEmpty() -> warningMessage.postValue("The list already contains this city")
                else -> {
                    val response = repo.getWeatherInfoByCityName(cityName)
                        response.data?.let {
                            latitude = it.coord.lat
                            longitude = it.coord.lon
                            searchForCityForecastByCityCoordinates(latitude, longitude)
                        } ?: warningMessage.postValue(response.message!!)

                }
            }
        }
    }

     fun searchForCityForecastByCityCoordinates(lat: String, long: String) {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeatherInfoByCityCoordinates(lat, long)
            if (response.data == null)
                warningMessage.postValue(response.message!!)
        }
    }

    fun deleteSwipedItem(cityId: Int){

        viewModelScope.launch(Dispatchers.IO){
            repo.deleteCity(cityId)
        }
    }

    fun getAllCities() = repo.getAllCities()

    suspend fun getCitiesCount() = repo.getCitiesCount()

    suspend fun getCityByItsId(id: Int) = repo.getCityByItsId(id)
}