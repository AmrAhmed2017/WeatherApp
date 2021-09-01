package com.example.parentapp.view.mainfragment

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
import com.example.parentapp.R
import com.example.parentapp.adapter.CityAdapter
import com.example.parentapp.databinding.FragmentMainBinding
import com.example.parentapp.model.PopulateObject
import com.example.parentapp.utils.SwipeToDeleteCallback
import com.example.parentapp.viewmodel.WeatherViewModel

class MainFragment : Fragment(), LocationListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: WeatherViewModel
    private lateinit var populateObject: List<PopulateObject>

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
                Log.v("===", "${populateObject[pos].id}")
                viewModel.deleteSwipedItem(populateObject[pos].id)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.citiesRecyclerView)

        getLocation()

        binding.searchBtn.setOnClickListener {
            if (binding.searchEditView.text.toString().trim().isEmpty())
                Toast.makeText(this.requireContext(), getString(R.string.search_area_is_empty), Toast.LENGTH_LONG).show()
            else{
                viewModel.fetchDataFromAPIByCityName(binding.searchEditView.text.toString())
                binding.searchEditView.text = null
            }
        }

        viewModel.countMessage.observe(viewLifecycleOwner, {
            AlertDialog.Builder(this.requireContext()).setTitle("warning").setMessage(it).
                    setPositiveButton("Ok") { dialog, _ -> dialog.dismiss() }.create().show()
        })

        viewModel.weatherLiveData.observe(viewLifecycleOwner, {
            populateObject = it
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
                viewModel.fetchDataFromAPIByCityNameAtDefaultMode()

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
                    } else{
                        viewModel.fetchDataFromAPIByCityNameAtDefaultMode()
                    }
                }.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        viewModel.fetchFutureWeatherInfoAtDefaultMode(location.latitude.toString(), location.longitude.toString())
    }
}