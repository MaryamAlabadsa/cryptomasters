package com.mas.cryptomasters.core.api

import com.mas.cryptomasters.data.models.PlanResponse
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.request.*
import com.mas.cryptomasters.data.response.*
import com.mas.cryptomasters.data.response.block.BlockResponse
import com.mas.cryptomasters.data.response.home.HomeResponse
import com.mas.cryptomasters.data.response.notifications.NotificationsResponse
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    @GET("settings")
    suspend fun getSettings(): Response<SettingsResponse>

    @GET("profile")
    suspend fun getProfile(): Response<ProfileResponse>

    @POST("profile/update")
    suspend fun updateProfile(@Body boy: EditProfileRequest): Response<ProfileResponse>


    @POST("login")
    suspend fun login(@Body any: Any): Response<ProfileResponse>

    @POST("register")
    suspend fun register(@Body any: Any): Response<ProfileResponse>

    @GET("home")
    suspend fun getHome(): Response<HomeResponse>

    @POST("contact")
    suspend fun contact(@Body body: ContactRequest): Response<BaseResponse>

    @POST("user/password")
    suspend fun changePassword(@Body body: ChangePasswordRequest): Response<BaseResponse>

    @POST("posts")
    suspend fun createPost(@Body body: CreatePostRequest): Response<BaseResponse>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: String): Response<BaseResponse>

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: String,
        @Body body: CreatePostRequest
    ): Response<UpdatePostResponse>

    @GET("posts/{id}")
    suspend fun postById(@Path("id") id: String): Response<PostResponse>

    @GET("recommendations")
    suspend fun getRecommendations(): Response<Recommendations>

    @POST("user/vote")
    suspend fun setVote(@Body body: VoteRequest): Response<BaseResponse>

    @GET("coins")
    suspend fun getCoins(
//        @Query("vs_currency") currency: String = "usd",
//        @Query("order") order: String = "market_cap_desc",
//        @Query("per_page") perPage: Int = 30,
//        @Query("page") page: Int = 1,
//        @Query("sparkline") sparkline: Boolean = false
    ): Response<CoinsResponse>

    @POST("like/post")
    suspend fun postLike(@Body body: LikePostRequest): Response<LikeResponse>

    @POST("dislike/post")
    suspend fun removeLike(@Body body: LikePostRequest): Response<BaseResponse>

    @GET("user/notifications")
    suspend fun getNotifications(): Response<NotificationsResponse>

//todo
    @POST("read/notifications")
    suspend fun readNotifications(@Body boy: ReadNotificationsRequest): Response<BaseResponse>

    @GET("delete/{id}}/notifications")
    suspend fun deleteNotifications(@Path("id") id: String): Response<BaseResponse>

    @POST("comment")
    suspend fun setComment(@Body body: CommentRequest): Response<BaseResponse>

    @DELETE("comment/{id}")
    suspend fun deleteComment(@Path("id") id: String): Response<BaseResponse>

    @PUT("comment/{id}")
    suspend fun editComment(
        @Path("id") id: String,
        @Body body: EditCommentRequest
    ): Response<BaseResponse>

    @POST("reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): Response<BaseResponse>


    @Multipart
    @POST("uploadVideo")
    suspend fun uploadVideo(
        @Part("video") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<DefaultResponse>

    @Multipart
    @POST("uploadImage")
    suspend fun uploadImage(
        @Part("image") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<DefaultResponse>

    @POST("visitor")
    suspend fun loginAsVisitor(@Body body: VisitorRequest): Response<BaseResponse>


    @POST("deleteImages")
    suspend fun deleteImages(@Body body: DeleteImagesRequest): Response<BaseResponse>


    @GET("block/{id}")
    suspend fun blockUser(@Path("id") id: String): Response<BaseResponse>

    @GET("blocked/list")
    suspend fun getBlockedList(): Response<BlockResponse>

    @GET("unblock/{id}")
    suspend fun unBlockUser(@Path("id") id: String): Response<BaseResponse>

   @GET("plans")
    suspend fun getMenuItems(): Response<PlanResponse>


}