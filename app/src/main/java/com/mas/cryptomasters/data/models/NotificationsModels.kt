package com.mas.cryptomasters.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationsModels(
    val action: String? = null,
    val post_id: String? = null
) : Parcelable