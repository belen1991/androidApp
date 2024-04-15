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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*


@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val cities = viewModel.cities.value
    val vehicles = viewModel.vehicles.value
    val reservationDates = viewModel.reservationDates

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        DropdownMenuCities(cities)
        DatePickers(reservationDates)
        VehicleList(vehicles)
    }
}

@Composable
fun DropdownMenuCities(cities: List<City>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.CenterStart) {
        Text(
            text = if (selectedCity.isEmpty()) "Select a city:" else selectedCity,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { expanded = true }) // Esto permite que el texto actúe como un botón desplegable
                .padding(8.dp)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            cities.forEach { city ->
                DropdownMenuItem(onClick = {
                    selectedCity = city.name
                    expanded = false
                }) {
                    Text(text = city.name)
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
            .fillMaxSize()
    ) {
        Row (horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
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
        Row (horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
            Text("Start Date: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(reservationDates.value.startDate)}", fontSize = 16.sp)
            Text("End Date: ${java.text.SimpleDateFormat("dd/MM/yyyy").format(reservationDates.value.endDate)}", fontSize = 16.sp)
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
            Text(text = "Car: ${vehicle.brand} ${vehicle.model}",
                modifier = Modifier
                    .padding(16.dp))
            Divider()
        }
    }
}
