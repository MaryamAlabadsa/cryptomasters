package com.mas.cryptomasters.data

data class Response<T>(
    var data: Any? = null,
    var error: String = "",
    var reLogin: Boolean = false,
    var flag: Int = 0,
    var list: ArrayList<String>? = null
)