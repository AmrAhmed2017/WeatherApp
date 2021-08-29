package com.example.parentapp.view.mainfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.parentapp.R
import com.example.parentapp.adapter.CityAdapter
import com.example.parentapp.databinding.FragmentMainBinding
import com.example.parentapp.utils.RecyclerItemDecoration
import com.example.parentapp.viewmodel.WeatherViewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
//    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        binding.citiesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.citiesRecyclerView.addItemDecoration(RecyclerItemDecoration(8))
//        binding.citiesRecyclerView.adapter = CityAdapter()

        binding.searchBtn.setOnClickListener {
            viewModel.fetchDataFromAPIByCityName(binding.searchEditView.text.toString())
        }

        viewModel.weatherLiveData.observe(viewLifecycleOwner, {
           it.forEach {
               Log.v("====", "${ it.id } ${ it.cityName } ${ it.latitude } ${ it.longitude } \n")
           }

        })
    }
}