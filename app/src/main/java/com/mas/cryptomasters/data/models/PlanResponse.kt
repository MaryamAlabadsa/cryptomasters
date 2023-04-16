package com.mas.cryptomasters.data.models

data class PlanResponse(
    val code: Int,
    val message: String,
    val success: String,
    val data: List<Plan>
)

data class Plan(
    val id: Int,
    val name: String,
    val body: String,
    val days: String,
    val ads_price: String,
    val recommendations_price: String,
    val total: String
)

