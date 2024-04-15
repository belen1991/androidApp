package com.example.alquicar.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alquicar.model.City
import com.example.alquicar.model.ReservationDates
import com.example.alquicar.model.Vehicle
import com.example.alquicar.viewmodel.HomeViewModel
import androidx.compose.material.Button
import androidx.compose.ui.platform.LocalContext
import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import java.util.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import com.google.firebase.firestore.GeoPoint

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val cities = viewModel.cities.value
    val vehicles = viewModel.vehicles.value
    val reservationDates = viewModel.reservationDates

    // Estado para mantener la ciudad seleccionada
    val selectedCity = remember { mutableStateOf("Todas") }  // Inicialmente "Todas"

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // Añade padding general para todo el Column
        verticalArrangement = Arrangement.spacedBy(16.dp), // Espacia uniformemente todos los componentes verticales
        horizontalAlignment = Alignment.Start
    ) {
        DropdownMenuCities(cities, selectedCity, viewModel)
        DatePickers(reservationDates)
        VehicleList(if (selectedCity.value == "Todas") vehicles else vehicles.filter { it.city == selectedCity.value })
    }
}

@Composable
fun DropdownMenuCities(cities: List<City>, selectedCity: MutableState<String>, viewModel: HomeViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val currentCityGeoPoint = remember { mutableStateOf<GeoPoint?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Seleccione una ciudad:",
            style = TextStyle(color = Color.Black, fontSize = 16.sp),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .clickable(onClick = { expanded = !expanded }),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = !expanded })
                    .padding(8.dp)
                    .background(if (expanded) Color.LightGray else Color.Transparent),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedCity.value,
                    style = TextStyle(color = Color.Black, fontSize = 16.sp),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = {
                    selectedCity.value = "Todas"
                    expanded = false
                    viewModel.loadVehicles()  // Carga todos los vehículos sin filtro
                }) {
                    Text(text = "Todas")
                }
                cities.forEach { city ->
                    DropdownMenuItem(onClick = {
                        selectedCity.value = city.name
                        expanded = false
                        viewModel.getVehiclesByCity(city.name)  // Actualiza los vehículos al seleccionar una ciudad específica
                    }) {
                        Text(text = city.name)
                    }
                }
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun DatePickers(reservationDates: MutableState<ReservationDates>) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // Establecer las fechas predeterminadas
    if (reservationDates.value.startDate == 0L) {
        val tomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 1)
        }
        reservationDates.value = reservationDates.value.copy(startDate = tomorrow.timeInMillis)
    }

    if (reservationDates.value.endDate == 0L) {
        val dayAfterTomorrow = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, 2)
        }
        reservationDates.value = reservationDates.value.copy(endDate = dayAfterTomorrow.timeInMillis)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp) // Añade padding vertical para separar este bloque de otros
    ) {
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
        {
            Button(onClick = {
                showDatePicker(context, true, reservationDates)
            }) {
                Text(text = "Select Start Date")
            }
            //Spacer(modifier = Modifier.height(16.dp))


            Button(onClick = {
                showDatePicker(context, false, reservationDates)
            }) {
                Text(text = "Select End Date")
            }
            //Spacer(modifier = Modifier.height(16.dp))

        }
        Spacer(modifier = Modifier
            .size(16.dp))
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly)
        {
            Text(java.text.SimpleDateFormat("dd/MM/yyyy").format(reservationDates.value.startDate), fontSize = 16.sp)
            Text(java.text.SimpleDateFormat("dd/MM/yyyy").format(reservationDates.value.endDate), fontSize = 16.sp)
        }
    }
}

fun showDatePicker(context: Context, isStart: Boolean, reservationDates: MutableState<ReservationDates>) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val selectedDate = calendar.apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, dayOfMonth)
            }.timeInMillis
            if (isStart) {
                reservationDates.value = reservationDates.value.copy(startDate = selectedDate)
            } else {
                reservationDates.value = reservationDates.value.copy(endDate = selectedDate)
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

@Composable
fun VehicleList(vehicles: List<Vehicle>) {
    LazyColumn {
        items(vehicles) { vehicle ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f) // Ocupa el espacio restante
                        .padding(end = 16.dp) // Espacio entre el texto y la imagen
                ) {
                    Text(text = "Vehículo: ${vehicle.brand} ${vehicle.model}")
                    Text(text = "Color: ${vehicle.color}")
                    Text(text = "Kilometraje: ${vehicle.mileage}")
                    Text(text = "Año: ${vehicle.year}")
                    Text(text = "Ciudad: ${vehicle.city}")
                }
                Image(
                    painter = rememberImagePainter(
                        data = vehicle.imageUrl,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = "Imagen del vehículo",
                    modifier = Modifier
                        .size(100.dp), // Tamaño de la imagen
                    contentScale = ContentScale.Crop
                )
            }
            Divider()
        }
    }
}