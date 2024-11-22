package com.example.inventory

// AuthViewModel.kt

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {
    val isLoggedIn = mutableStateOf(false)

    fun loginUser() {
        isLoggedIn.value = true
    }

    fun logoutUser() {
        isLoggedIn.value = false
    }
}
