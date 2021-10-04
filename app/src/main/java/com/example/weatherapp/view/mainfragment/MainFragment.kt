package com.example.weatherapp.view.mainfragment

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.adapter.CityAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.model.CityEntity
import com.example.weatherapp.utils.SwipeToDeleteCallback
import com.example.weatherapp.viewmodel.WeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment(), LocationListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var helperObject: List<CityEntity>

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

        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        binding.citiesRecyclerView.layoutManager = LinearLayoutManager(requireActivity())


        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                Log.v("===", "${helperObject[pos].id}")
                viewModel.deleteSwipedItem(helperObject[pos].id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.citiesRecyclerView)

        getLocation()

        binding.searchBtn.setOnClickListener {
            if (binding.searchEditView.text.toString().trim().isEmpty())
                Toast.makeText(this.requireContext(), getString(R.string.search_area_is_empty), Toast.LENGTH_LONG).show()
            else{
                viewModel.searchForCityForecastByCityName(binding.searchEditView.text.toString())
                binding.searchEditView.text = null
            }
        }

        viewModel.warningMessage.observe(viewLifecycleOwner, {
            it?.let {
                AlertDialog.Builder(this.requireContext()).setTitle("warning").setMessage(it).
                setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                    viewModel.warningMessage.value = null
                }.create().show()
            }
        })

        viewModel.getAllCities().observe(viewLifecycleOwner, {
            helperObject = it
            binding.citiesRecyclerView.adapter = CityAdapter(it){id -> onListItemClick(id)}
        })
    }

    private fun onListItemClick(id: Int) {
        val bundle = bundleOf("id" to id)
        findNavController(this).navigate(R.id.action_mainFragment_to_forecastFragment, bundle)
    }

    private fun getLocation() {
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        when{
            !locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                GlobalScope.launch(Dispatchers.Main) {
                    if (viewModel.getCitiesCount() == 0)
                        viewModel.searchForCityForecastByCityName()
                }

            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                    activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED ->
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                    activity?.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            -> {
                registerForActivityResult(ActivityResultContracts.RequestPermission()){
                    if (it){
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
                        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    }
                    else{
                        GlobalScope.launch(Dispatchers.Main) {
                            if (viewModel.getCitiesCount() == 0)
                                viewModel.searchForCityForecastByCityName()
                        }
                    }
                }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        GlobalScope.launch(Dispatchers.Main) {
            if (viewModel.getCitiesCount() == 0)
                viewModel.searchForCityForecastByCityCoordinates(location.latitude.toString(), location.longitude.toString())
        }
    }
}