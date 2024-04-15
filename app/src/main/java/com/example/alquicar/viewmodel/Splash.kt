package com.example.alquicar.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Splash : ViewModel() {

    var isSplashScreen : Boolean by mutableStateOf(false)

    init{
        viewModelScope.launch {
            delay(2000)
            isSplashScreen = false
        }
    }
}