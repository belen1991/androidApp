package com.example.alquicar.viewmodels

import android.content.Context
import android.location.Geocoder
import java.util.Locale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquicar.data.Vehicle
import com.example.alquicar.utils.VehicleRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VehicleViewModel(private val context: Context) : ViewModel() {
    private val repository = VehicleRepository()
    private val _vehicles = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicles: StateFlow<List<Vehicle>> = _vehicles.asStateFlow()

    init {
        loadVehicles()
    }

    suspend fun getCityFromLatLng(latlng: LatLng): String? = withContext(Dispatchers.IO) {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(latlng.latitude, latlng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                return@withContext addresses[0].locality
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            _vehicles.value = repository.getVehicles()
        }
    }

    fun loadVehiclesByCity(city: String) {
        viewModelScope.launch {
            _vehicles.emit(repository.getVehiclesByCity(city))
        }
    }
}