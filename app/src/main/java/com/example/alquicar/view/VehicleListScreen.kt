package com.example.alquicar.view


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alquicar.model.Vehicle
import com.example.alquicar.viewmodel.VehicleViewModel
import com.google.android.gms.maps.model.LatLng

@Composable
fun VehicleItem(vehicle: Vehicle) {
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = "Make: ${vehicle.brand}", style = MaterialTheme.typography.body1)
        Text(text = "Model: ${vehicle.model}", style = MaterialTheme.typography.body2)
        Text(text = "Year: ${vehicle.year}", style = MaterialTheme.typography.body2)
        Text(text = "City: ${vehicle.city}", style = MaterialTheme.typography.body2)
    }
}

@Composable
fun VehicleListScreen(vehicleViewModel: VehicleViewModel = hiltViewModel()) {

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    val context = LocalContext.current
    var city by remember { mutableStateOf("DefaultCity") }

    val vehicles by vehicleViewModel.vehicles.collectAsState()

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let {
            city = vehicleViewModel.getCityFromLatLng(it) ?: "DefaultCity"
            vehicleViewModel.loadVehiclesByCity(city)
        }
    }

    Column {
        MapScreen { location ->
            selectedLocation = location
        }
        LazyColumn {
            items(vehicles) { vehicle ->
                VehicleItem(vehicle)
            }
        }
    }
}

