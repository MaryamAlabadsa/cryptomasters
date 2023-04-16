package com.mas.cryptomasters.data.response.block


import com.google.gson.annotations.SerializedName
import com.mas.cryptomasters.data.response.BaseResponse

data class BlockResponse(
    @SerializedName("data")
    val data: List<BlockData>,
): BaseResponse()


data class BlockData(
    @SerializedName("block")
    val blockObject: BlockObject,
    @SerializedName("block_id")
    val blockId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)