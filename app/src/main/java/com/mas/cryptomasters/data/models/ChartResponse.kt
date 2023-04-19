package com.mas.cryptomasters.data.models

data class ChartResponse(
//    val prices: List<Map.Entry<Long, Double>>,
//    val marketCaps: List<Map.Entry<Long, Double>>,
//    val totalVolumes: List<Map.Entry<Long, Double>>
    val prices: List<List<Any>>,
    val marketCaps: List<List<Any>>,
    val totalVolumes: List<List<Any>>,

)
data class Price(
    val timestamp: Double,
    val value: Double
)