package com.example.alquicar.model

data class Vehicle(
    val id: String = "",
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val city: String = "",
    val color: String = "",
    val mileage: Int = 0,
    val doors: Int = 0,
    val imageUrl: String ="",
    val ubicacion: GeoPoint  // GeoPoint to store latitude and longitude
)

data class GeoPoint(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)