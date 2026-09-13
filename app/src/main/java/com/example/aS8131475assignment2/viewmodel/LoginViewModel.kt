package com.example.aS8131475assignment2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aS8131475assignment2.data.LoginRequest
import com.example.aS8131475assignment2.network.ApiService
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val keypass: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class LoginViewModel(private val apiService: ApiService) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(LoginState.Idle)
    val loginState: LiveData<LoginState> = _loginState

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Please enter both fields")
            return
        }

        _loginState.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val response = apiService.login(LoginRequest(username, password))
                _loginState.value = LoginState.Success(response.keypass)
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login failed: ${e.message ?: "Unknown error"}")
            }
        }
    }
}