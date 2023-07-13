package com.mas.cryptomasters.core.api

import com.mas.cryptomasters.data.models.PlanResponse
import com.mas.cryptomasters.data.request.*
import com.mas.cryptomasters.data.request.LikePostRequest
import com.mas.cryptomasters.data.response.*
import com.mas.cryptomasters.data.response.block.BlockResponse
import com.mas.cryptomasters.data.response.notifications.NotificationsResponse
import com.mas.cryptomasters.data.response.recommendations.Recommendations
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSettings() = apiService.getSettings()
    suspend fun getProfile() = apiService.getProfile()
    suspend fun updateProfile(body: EditProfileRequest): Response<ProfileResponse> =
        apiService.updateProfile(body)

    suspend fun login(body: Any) = apiService.login(body)
    suspend fun loginByGoogle(body: Any) = apiService.loginByGoogle(body)
    suspend fun register(body: Any): Response<ProfileResponse> = apiService.register(body)
    suspend fun getHome() = apiService.getHome()
    suspend fun contact(body: ContactRequest) = apiService.contact(body)
    suspend fun changePassword(body: ChangePasswordRequest) = apiService.changePassword(body)
    suspend fun createPost(body: CreatePostRequest) = apiService.createPost(body)
    suspend fun deletePost(id: String) = apiService.deletePost(id)
    suspend fun updatePost(id: String, body: CreatePostRequest): Response<UpdatePostResponse> =
        apiService.updatePost(id, body)

    suspend fun getDailyRecommendations(page: Int = 1): Response<Recommendations> = apiService.getDailyRecommendations(page)
    suspend fun getMonthlyRecommendations(page: Int = 1): Response<Recommendations> = apiService.getMonthlyRecommendations(page)
    suspend fun getWeaklyRecommendations(page: Int = 1): Response<Recommendations> = apiService.getWeaklyRecommendations(page)
    suspend fun getAnalytic(): Response<Analytic> = apiService.getAnalytic()
    suspend fun getRecommendations(): Response<Recommendations> = apiService.getRecommendations()
    suspend fun setVote(body: VoteRequest): Response<BaseResponse> = apiService.setVote(body)
    suspend fun getCoins(): Response<CoinsResponse> = apiService.getCoins()
    suspend fun searchCoins(query: SearchRequest): Response<SearchResponse> = apiService.searchCoins(query)
    suspend fun getMenuItems(): Response<PlanResponse> = apiService.getMenuItems()
    suspend fun postLike(body: LikePostRequest): Response<LikeResponse> = apiService.postLike(body)
    suspend fun removeLike(body: LikePostRequest): Response<BaseResponse> =
        apiService.removeLike(body)

    suspend fun setComment(body: CommentRequest): Response<BaseResponse> =
        apiService.setComment(body)

    suspend fun deleteComment(id: String): Response<BaseResponse> = apiService.deleteComment(id)
    suspend fun editComment(id: String, body: EditCommentRequest): Response<BaseResponse> =
        apiService.editComment(id, body)

    suspend fun postById(id: String): Response<PostResponse> = apiService.postById(id)
    suspend fun uploadVideo(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<DefaultResponse> = apiService.uploadVideo(description, file)

    suspend fun uploadImage(
        description: RequestBody,
        file: MultipartBody.Part
    ): Response<DefaultResponse> = apiService.uploadImage(description, file)


    suspend fun getNotifications(): Response<NotificationsResponse> =
        apiService.getNotifications()

//    suspend fun getCoin(): Response<Coins> =
//        apiService.getCoin("usd","market_cap_desc","30","1","false")

    suspend fun deleteNotifications(id: String): Response<BaseResponse> =
        apiService.deleteNotifications(id)

    suspend fun readNotifications(body: ReadNotificationsRequest): Response<BaseResponse> =
        apiService.readNotifications(body)

    suspend fun resetPassword(body: ResetPasswordRequest): Response<BaseResponse> =
        apiService.resetPassword(body)

    suspend fun loginAsVisitor(body: VisitorRequest): Response<BaseResponse> =
        apiService.loginAsVisitor(body)

    suspend fun deleteImages(body: DeleteImagesRequest): Response<BaseResponse> =
        apiService.deleteImages(body)

    suspend fun blockUser(id: String): Response<BaseResponse> = apiService.blockUser(id)

    suspend fun getBlockList(): Response<BlockResponse> = apiService.getBlockedList()

    suspend fun unBlockUser(id: String): Response<BaseResponse> = apiService.unBlockUser(id)

}