package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.databinding.WeatherRecyclerItemBinding
import com.example.weatherapp.model.DailyWeather
//import com.example.weatherapp.model.WeatherEntity

class WeatherAdapter(val data: List<DailyWeather>): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private lateinit var binding: WeatherRecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.weather_recycler_item, parent, false)
        return  WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {

        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position

    class WeatherViewHolder(
        private val binding: WeatherRecyclerItemBinding,
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: DailyWeather) {
            binding.apply {

                binding.dayTextView.text = item.dt
                binding.tempTextView.text = String.format(
                    App.applicationContext().resources.getString(R.string.temp)
                    , item.temp.min, item.temp.max)
                binding.pressureTextValue.text = item.pressure
                binding.humidityTextValue.text = item.humidity
                binding.windTextValue.text = item.wind_speed
                binding.descTextValue.text = item.weather[0].description
            }
        }
    }
}