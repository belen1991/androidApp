package com.example.alquicar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquicar.model.Reservation
import com.example.alquicar.repository.ReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReservationViewModel @Inject constructor(private val reservationRepository: ReservationRepository) : ViewModel() {
    private val _reservations = MutableStateFlow<List<Reservation>>(emptyList())
    val reservations = _reservations.asStateFlow()

    fun loadReservationsByUser(userId: String) {
        viewModelScope.launch {
            _reservations.value = reservationRepository.getReservationsByUser(userId)
        }
    }
}
