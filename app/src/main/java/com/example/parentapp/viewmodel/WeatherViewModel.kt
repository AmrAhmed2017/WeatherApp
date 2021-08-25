package com.example.parentapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parentapp.model.FutureWeatherResponse
import com.example.parentapp.model.WeatherResponse
import com.example.parentapp.repo.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val repo = DataRepository
    val weatherLiveData = MutableLiveData<WeatherResponse>()
    val futureWeatherLiveData = MutableLiveData<FutureWeatherResponse>()
//    val offersLiveData = MutableLiveData<OfferResponse>()

    fun fetchDataFromAPIByCityName() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeatherInfoByCityName()
            weatherLiveData.postValue(response)

        }
    }

    fun fetchDataFromAPIByCoordinates() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getWeatherInfoByCoordinates()
            weatherLiveData.postValue(response)

        }
    }

    fun fetchFutureWeatherInfo() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getNextWeatherInfo()
            futureWeatherLiveData.postValue(response)

        }
    }

//    private fun fetchCategoriesFromAPIAsync(baseURL: String, endPoint: String) =
//        viewModelScope.async {
//            repo.fetchCategories(baseURL, endPoint)
//        }

//    private fun fetchOffersFromAPIAsync(baseURL: String, endPoint: String) =
//        viewModelScope.async {
//            repo.fetchOffers(baseURL, endPoint)
//        }


//    private fun populateData(categoriesResponse: CategoriesResponse, offerResponse: OfferResponse) {
//        categoriesLiveData.postValue(categoriesResponse)
//        offersLiveData.postValue(offerResponse)
//    }
}