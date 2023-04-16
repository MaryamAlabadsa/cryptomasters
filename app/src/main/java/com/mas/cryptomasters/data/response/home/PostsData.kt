package com.mas.cryptomasters.data.response.home
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostsData(
    @SerializedName("comments")
    var comments: ArrayList<Comment>?,
    @SerializedName("comments_count")
    var commentsCount: String?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("gallaries")
    val galleries: List<Galleries>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("isLike")
    var isLike: Int?,
    @SerializedName("likes")
    var likes: ArrayList<Like>?,
    @SerializedName("likes_count")
    var likesCount: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("user")
    val user: User?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("video")
    val video: String?
) : Parcelable