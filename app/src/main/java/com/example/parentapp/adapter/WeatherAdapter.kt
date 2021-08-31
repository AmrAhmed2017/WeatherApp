package com.example.parentapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.parentapp.R
import com.example.parentapp.databinding.RecyclerItemBinding
import com.example.parentapp.databinding.WeatherRecyclerItemBinding
import com.example.parentapp.model.PopulateObject
import com.example.parentapp.model.WeatherEntity

class WeatherAdapter(val data: List<WeatherEntity>): RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

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

        fun bind(item: WeatherEntity) {
            binding.apply {

                binding.dayTextView.text = item.timestamp
                binding.tempTextView.text = item.minTemp + " / " + item.maxTemp
            }
        }
    }
}