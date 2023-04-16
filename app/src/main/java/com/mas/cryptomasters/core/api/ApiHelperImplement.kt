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
import javax.inject.Inject

class ApiHelperImplement @Inject constructor(private val apiService: ApiService) : ApiHelper {
    override suspend fun getSettings(): Response<SettingsResponse> = apiService.getSettings()
    override suspend fun getProfile(): Response<ProfileResponse> = apiService.getProfile()
    override suspend fun updateProfile(body: EditProfileRequest): Response<ProfileResponse> =
        apiService.updateProfile(body)

    override suspend fun login(body: Any): Response<ProfileResponse> = apiService.login(body)
    override suspend fun register(body: Any): Response<ProfileResponse> = apiService.register(body)
    override suspend fun getHome(): Response<HomeResponse> = apiService.getHome()
    override suspend fun contact(body: ContactRequest): Response<BaseResponse> =
        apiService.contact(body)

    override suspend fun changePassword(body: ChangePasswordRequest): Response<BaseResponse> =
        apiService.changePassword(body)

    override suspend fun createPost(body: CreatePostRequest): Response<BaseResponse> =
        apiService.createPost(body)

    override suspend fun deletePost(id: String): Response<BaseResponse> = apiService.deletePost(id)
    override suspend fun updatePost(
        id: String,
        body: CreatePostRequest
    ): Response<UpdatePostResponse> =
        apiService.updatePost(id, body)

    override suspend fun getRecommendations(): Response<Recommendations> =
        apiService.getRecommendations()

    override suspend fun setVote(body: VoteRequest): Response<BaseResponse> =
        apiService.setVote(body)

    override suspend fun getCoins(): Response<CoinsResponse> = apiService.getCoins()
    override suspend fun postLike(body: LikePostRequest): Response<LikeResponse> =
        apiService.postLike(body)

    override suspend fun removeLike(body:LikePostRequest): Response<BaseResponse> =
        apiService.removeLike(body)


    override suspend fun setComment(body: CommentRequest): Response<BaseResponse> =
        apiService.setComment(body)

    override suspend fun deleteComment(id: String): Response<BaseResponse> =
        apiService.deleteComment(id)

    override suspend fun editComment(id: String, body: EditCommentRequest): Response<BaseResponse> =
        apiService.editComment(id, body)

    override suspend fun postById(id: String): Response<PostResponse> = apiService.postById(id)


    override suspend fun getNotifications(): Response<NotificationsResponse> =
        apiService.getNotifications()

    override suspend fun readNotifications(body: ReadNotificationsRequest): Response<BaseResponse> =
        apiService.readNotifications(body)

    override suspend fun deleteNotifications(id: String): Response<BaseResponse> =
        apiService.deleteNotifications(id)

    override suspend fun resetPassword(body: ResetPasswordRequest): Response<BaseResponse> =
        apiService.resetPassword(body)

    override suspend fun uploadVideo(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<DefaultResponse> = apiService.uploadVideo(description, file)

    override suspend fun uploadImage(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<DefaultResponse> = apiService.uploadImage(description, file)

    override suspend fun loginAsVisitor(body: VisitorRequest): Response<BaseResponse> = apiService.loginAsVisitor(body)
    override suspend fun deleteImages(body: DeleteImagesRequest): Response<BaseResponse>  = apiService.deleteImages(body)
    override suspend fun blockUser(id: String): Response<BaseResponse>  = apiService.blockUser(id)
    override suspend fun getBlockList(): Response<BlockResponse> = apiService.getBlockedList()
    override suspend fun unBlockUser(id: String): Response<BaseResponse> = apiService.unBlockUser(id)
}