package com.mas.cryptomasters.utils

data class ClickAction(
    val position: Int =-1,
    val event: ClickEvent,
    val data: Any? =null
)
