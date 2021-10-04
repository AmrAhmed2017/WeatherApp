package com.example.weatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.App
import com.example.weatherapp.R
import com.example.weatherapp.databinding.RecyclerItemBinding
import com.example.weatherapp.model.CityEntity

class CityAdapter(val data: List<CityEntity>, private val onItemClicked: (Int) -> Unit): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private lateinit var binding: RecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item, parent, false)
        return  CityViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {

        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    override fun getItemViewType(position: Int) = position

    class CityViewHolder(
        private val binding: RecyclerItemBinding,
        private val onItemClicked: (Int) -> Unit
    ): RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        private var id: Int
        init {
            id = 0
            itemView.setOnClickListener(this)
        }
        fun bind(item: CityEntity) {
            binding.apply {
                id = item.id

                binding.cityTextView.text = item.cityName
                binding.tempTextView.text = String.format(App.applicationContext().resources.getString(R.string.temp)
                    , item.dailyWeather[0].temp.min, item.dailyWeather[0].temp.max)
            }
        }

        override fun onClick(v: View?) {
            onItemClicked(id)
        }
    }
}