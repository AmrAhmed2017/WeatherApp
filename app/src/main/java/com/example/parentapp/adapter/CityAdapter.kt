package com.example.parentapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.parentapp.R
import com.example.parentapp.databinding.RecyclerItemBinding
import com.example.parentapp.model.WeatherResponse

class CityAdapter(val data: WeatherResponse, private val context: Context): RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    private lateinit var binding: RecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.recycler_item, parent, false)
        return  CityViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {

        holder.bind(data)
    }

    override fun getItemCount() = 1

    override fun getItemViewType(position: Int) = position

    class CityViewHolder(
        private val binding: RecyclerItemBinding,
        private val context: Context
    ): RecyclerView.ViewHolder(binding.root){

        fun bind(item: WeatherResponse) {
            binding.apply {
                binding.textView.text = item.name
            }
        }
    }
}