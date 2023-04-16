package com.mas.cryptomasters.utils

import com.mas.cryptomasters.data.response.home.Comment
import com.mas.cryptomasters.data.response.home.Like
import com.mas.cryptomasters.data.response.home.PostsData


data class UpdateData(
    val whereData: PostsData? = null,
    val isThereRecommendUpdate: Boolean? = null
)