package com.example.alquicar.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.alquicar.model.Reservation
import com.example.alquicar.viewmodel.ReservationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReservationScreen(viewModel: ReservationViewModel = hiltViewModel()) {
    val reservations by viewModel.reservations.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Your Reservations",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(reservations) { reservation ->
                ReservationItem(reservation)
            }
        }
    }
}

@Composable
fun ReservationItem(reservation: Reservation) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Reservation ID: ${reservation.reservationId}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Vehicle ID: ${reservation.vehicleId}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "From: ${formatDate(reservation.startDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "To: ${formatDate(reservation.endDate)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${reservation.status}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    return formatter.format(date)
}