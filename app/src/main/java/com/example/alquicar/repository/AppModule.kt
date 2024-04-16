package com.example.alquicar.repository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)  // Indica que el ámbito es a nivel de aplicación
object AppModule {

    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideUserProfileRepository(db: FirebaseFirestore): UserProfileRepository = UserProfileRepository(db)

    @Provides
    fun provideReservationRepository(db: FirebaseFirestore): ReservationRepository = ReservationRepository(db)
}
