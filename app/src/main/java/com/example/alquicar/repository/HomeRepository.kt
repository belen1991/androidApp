package com.example.alquicar.repository

import com.example.alquicar.model.City
import com.example.alquicar.model.Vehicle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class HomeRepository @Inject constructor() {
    private val db = FirebaseFirestore.getInstance()
    private val vehicleCollection = db.collection("vehicle")
    private val cityCollection = db.collection("city")
    suspend fun getAllCities(): List<City> {
        return try {
            cityCollection.get().await().documents.map { document ->
                document.toObject(City::class.java)!!
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getVehicles(): List<Vehicle> {
        return try {
            vehicleCollection.get().await().documents.map { document ->
                document.toObject(Vehicle::class.java)!!
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getVehiclesByCity(city: String): List<Vehicle> {
        // Fetch vehicles and filter by city
        return db.collection("vehicles")
            .whereEqualTo("city", city)
            .get()
            .await()
            .documents.mapNotNull { it.toObject(Vehicle::class.java) }
    }
}