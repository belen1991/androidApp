package com.example.alquicar.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alquicar.model.UserProfile
import com.example.alquicar.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository
    ) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile = _userProfile.asStateFlow()

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _userProfile.value = userProfileRepository.getUserProfile(userId)
        }
    }

    fun updateUserProfile(userId: String, userProfile: UserProfile) {
        viewModelScope.launch {
            val success = userProfileRepository.updateUserProfile(userId, userProfile)
            if (success) {
                _userProfile.value = userProfile
            }
        }
    }

    fun loadUserProfileByEmail(email: String) {
        viewModelScope.launch {
            _userProfile.value = userProfileRepository.getUserProfileByEmail(email)
        }
        Log.w("", "USER PROFILE:"+_userProfile.value)
    }
}
