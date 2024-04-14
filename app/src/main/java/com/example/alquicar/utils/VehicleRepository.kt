package com.example.alquicar.utils

import com.example.alquicar.data.Vehicle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class VehicleRepository {
    private val db = FirebaseFirestore.getInstance()
    private val vehicleCollection = db.collection("vehicles")

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
