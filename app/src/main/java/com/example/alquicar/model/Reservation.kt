package com.example.alquicar.model

import java.util.Date

data class Reservation(
    val reservationId: String,
    val vehicleId: String,
    val userId: String,
    val startDate: Date,
    val endDate: Date,
    val status: String  // Ejemplo: "Confirmed", "Pending", "Cancelled"
)

