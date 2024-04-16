package com.example.alquicar.repository

import android.util.Log
import com.example.alquicar.model.UserProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserProfileRepository @Inject constructor(private val db: FirebaseFirestore) {
    private val usersCollection = db.collection("user")

    suspend fun getUserProfile(userId: String): UserProfile? {
        return try {
            Log.w("", "USER ID:"+userId)
            usersCollection.document(userId).get().await()
                .toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(userId: String, userProfile: UserProfile): Boolean {
        return try {
            usersCollection.document(userId).set(userProfile).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserProfileByEmail(email: String): UserProfile? {
        return try {
            Log.w("", "EMAIL:"+email)
            db.collection("user")
                .whereEqualTo("email", email)
                .limit(1)
                .get()
                .await()
                .documents.firstOrNull()
                ?.toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }
}
