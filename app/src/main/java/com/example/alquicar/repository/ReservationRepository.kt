package com.example.alquicar.repository

import com.example.alquicar.model.Reservation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReservationRepository @Inject constructor(private val db: FirebaseFirestore) {
    private val reservationsCollection = db.collection("reservations")

    suspend fun getReservationsByUser(userId: String): List<Reservation> {
        return try {
            reservationsCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(Reservation::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Métodos adicionales para crear y actualizar reservas
}