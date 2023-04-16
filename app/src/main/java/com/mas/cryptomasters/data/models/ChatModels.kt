package com.mas.cryptomasters.data.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class ChatModels(
    var avatar: String = "",
    var idUser: Int = -1,
    var message: String = "",
    var name: String = "",
    @ServerTimestamp
    val timestamp: Date? = null
)