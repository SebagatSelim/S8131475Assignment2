package com.example.aS8131475assignment2.network

import com.example.aS8131475assignment2.data.DashboardResponse
import com.example.aS8131475assignment2.data.LoginRequest
import com.example.aS8131475assignment2.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("sydney/auth")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse

}