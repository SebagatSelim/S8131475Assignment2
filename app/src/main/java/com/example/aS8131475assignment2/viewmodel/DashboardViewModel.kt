package com.example.aS8131475assignment2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aS8131475assignment2.network.ApiService
import kotlinx.coroutines.launch

sealed class DashboardState {
    object Loading : DashboardState()
    data class Success(val entities: List<Map<String, String>>) : DashboardState()
    data class Error(val message: String) : DashboardState()
}

class DashboardViewModel(private val apiService: ApiService) : ViewModel() {

    private val _dashboardState = MutableLiveData<DashboardState>()
    val dashboardState: LiveData<DashboardState> = _dashboardState

    fun loadDashboard(keypass: String) {
        _dashboardState.value = DashboardState.Loading

        viewModelScope.launch {
            try {
                val response = apiService.getDashboard(keypass)
                _dashboardState.value = DashboardState.Success(response.entities)
            } catch (e: Exception) {
                _dashboardState.value = DashboardState.Error("Failed to load dashboard: ${e.message ?: "Unknown error"}")
            }
        }
    }
}