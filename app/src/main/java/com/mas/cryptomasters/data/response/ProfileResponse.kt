package com.mas.cryptomasters.data.response


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.home.PostsData

data class ProfileResponse(
    @SerializedName("data")
    val profileObject: ProfileObject? = ProfileObject(),
) : BaseResponse()

data class ProfileObject(
    @SerializedName("id"            ) var id           : Int?              = null,
    @SerializedName("name"          ) var name         : String?           = null,
    @SerializedName("phone"         ) var phone        : String?           = null,
    @SerializedName("email"         ) var email        : String?           = null,
    @SerializedName("registerBy"    ) var registerBy   : String?           = null,
    @SerializedName("api_token"     ) var apiToken     : String?           = null,
    @SerializedName("avatar"        ) var avatar       : String?           = null,
    @SerializedName("device_token"  ) var deviceToken  : String?           = null,
    @SerializedName("isCompleted"   ) var isCompleted  : String?           = null,
    @SerializedName("isActive"      ) var isActive     : String?           = null,
    @SerializedName("isPaid"        ) var isPaid       : String?           = null,
    @SerializedName("subscribe_end" ) var subscribeEnd : String?           = null,
    @SerializedName("created_at"    ) var createdAt    : String?           = null,
    @SerializedName("updated_at"    ) var updatedAt    : String?           = null,
    @SerializedName("posts"         ) var posts        : ArrayList<PostsData> = arrayListOf()

)