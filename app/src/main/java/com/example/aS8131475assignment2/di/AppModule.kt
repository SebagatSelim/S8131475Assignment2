package com.example.aS8131475assignment2.di

import com.example.aS8131475assignment2.network.ApiService
import com.example.aS8131475assignment2.network.RetrofitInstance
import com.example.aS8131475assignment2.viewmodel.DashboardViewModel
import com.example.aS8131475assignment2.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ApiService> { RetrofitInstance.api }
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}