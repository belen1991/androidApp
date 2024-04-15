package com.example.alquicar.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquicar.model.City
import com.example.alquicar.model.ReservationDates
import com.example.alquicar.model.Vehicle
import com.example.alquicar.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {
    private val _cities = mutableStateOf<List<City>>(emptyList())
    val cities: State<List<City>> = _cities

    private val _vehicles = mutableStateOf<List<Vehicle>>(emptyList())
    val vehicles: State<List<Vehicle>> = _vehicles

    var reservationDates = mutableStateOf(ReservationDates(0, 0))

    init {
        loadCities()
        loadVehicles()
    }

    private fun loadCities() {
        viewModelScope.launch {
            _cities.value = homeRepository.getAllCities()
        }
    }

    private fun loadVehicles() {
        viewModelScope.launch {
            _vehicles.value = homeRepository.getVehicles()
        }
    }
}