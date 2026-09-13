package com.example.aS8131475assignment2.data

data class DashboardResponse(
    val entities: List<Map<String, String>>,
    val entityTotal: Int
)