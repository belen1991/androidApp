package com.example.alquicar.service

import com.example.alquicar.model.Vehicle
import retrofit2.http.GET

interface VehicleService {
    @GET("vehicles")
    suspend fun getVehicles(): Vehicle
}