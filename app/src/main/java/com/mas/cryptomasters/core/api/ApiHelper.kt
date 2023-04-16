package com.mas.cryptomasters.core.api

import com.mas.cryptomasters.data.request.*
import com.mas.cryptomasters.data.response.*
import com.mas.cryptomasters.data.response.block.BlockResponse
import com.mas.cryptomasters.data.response.home.HomeResponse
import com.mas.cryptomasters.data.response.notifications.NotificationsResponse
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

interface ApiHelper {
    suspend fun getSettings(): Response<SettingsResponse>
    suspend fun getProfile(): Response<ProfileResponse>
    suspend fun updateProfile(body: EditProfileRequest): Response<ProfileResponse>
    suspend fun login(body: Any): Response<ProfileResponse>
    suspend fun register(body: Any): Response<ProfileResponse>
    suspend fun getHome(): Response<HomeResponse>
    suspend fun contact(body: ContactRequest): Response<BaseResponse>
    suspend fun changePassword(body: ChangePasswordRequest): Response<BaseResponse>
    suspend fun createPost(body: CreatePostRequest): Response<BaseResponse>
    suspend fun deletePost(id: String): Response<BaseResponse>
    suspend fun updatePost(id: String, body: CreatePostRequest): Response<UpdatePostResponse>
    suspend fun getRecommendations(): Response<Recommendations>
    suspend fun setVote(body: VoteRequest): Response<BaseResponse>
    suspend fun getCoins(): Response<CoinsResponse>
    suspend fun postLike(body: LikePostRequest): Response<LikeResponse>
    suspend fun removeLike(body: LikePostRequest): Response<BaseResponse>
    suspend fun setComment(body: CommentRequest): Response<BaseResponse>
    suspend fun deleteComment(id: String): Response<BaseResponse>
    suspend fun editComment(id: String, body: EditCommentRequest): Response<BaseResponse>
    suspend fun postById(id: String): Response<PostResponse>
    suspend fun getNotifications(): Response<NotificationsResponse>
    suspend fun readNotifications(body: ReadNotificationsRequest): Response<BaseResponse>
    suspend fun deleteNotifications(id: String): Response<BaseResponse>
    suspend fun resetPassword(body: ResetPasswordRequest): Response<BaseResponse>
    suspend fun uploadVideo(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<DefaultResponse>

    suspend fun uploadImage(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<DefaultResponse>

    suspend fun loginAsVisitor(body: VisitorRequest): Response<BaseResponse>
    suspend fun deleteImages(body: DeleteImagesRequest): Response<BaseResponse>
    suspend fun blockUser(id: String): Response<BaseResponse>
    suspend fun getBlockList(): Response<BlockResponse>
    suspend fun unBlockUser(id : String) : Response<BaseResponse>
}