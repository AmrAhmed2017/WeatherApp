package com.example.parentapp.view.forecastfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parentapp.R
import com.example.parentapp.adapter.WeatherAdapter
import com.example.parentapp.databinding.FragmentForecastBinding
import com.example.parentapp.mapper.FutureWeatherMapper
import com.example.parentapp.viewmodel.WeatherViewModel

class ForecastFragment : Fragment() {

    private lateinit var binding: FragmentForecastBinding
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = arguments?.get("id").toString()
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel.getWeatherForecast(id.toInt())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_forecast, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.weatherRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        viewModel.forecastLiveData.observe(viewLifecycleOwner, {
            val weatherEntities = it.map {
                return@map FutureWeatherMapper.selfMap(it)
            }
            binding.weatherRecyclerView.adapter = WeatherAdapter(weatherEntities)
        })
    }
}