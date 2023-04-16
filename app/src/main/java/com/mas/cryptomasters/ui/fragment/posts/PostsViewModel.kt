package com.mas.cryptomasters.ui.fragment.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mas.cryptomasters.core.api.ApiRepository
import com.mas.cryptomasters.data.Response
import com.mas.cryptomasters.data.request.*
import com.mas.cryptomasters.data.response.EditCommentRequest
import com.mas.cryptomasters.data.response.home.Comment
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.utils.Extensions.isRequestSuccess
import com.mas.cryptomasters.utils.RESPONSE.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private var apiRepository: ApiRepository) : ViewModel() {

    var postStatusMutable = MutableLiveData<Response<Any?>>()
    var postFromDetailsMutable = MutableLiveData<Response<Any?>>()

    lateinit var commentRequest: CommentRequest
    lateinit var likePostRequest: LikePostRequest
    lateinit var postRequest: CreatePostRequest
    lateinit var editComment: EditCommentRequest

    fun createPost() {
        viewModelScope.launch {
            apiRepository.createPost(postRequest)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postStatusMutable.postValue(Response(reLogin = true))
                        ERROR -> postStatusMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postStatusMutable.postValue(
                            Response(
                                data = it.body(),
                                flag = 200
                            )
                        )
                    }
                }
        }
    }

    fun updateCurrentPost(id: String) {
        viewModelScope.launch {
            apiRepository.updatePost(id, postRequest)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postStatusMutable.postValue(Response(reLogin = true))
                        ERROR -> postStatusMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postStatusMutable.postValue(
                            Response(
                                data = id,
                                flag = 201
                            )
                        )
                    }
                }
        }
    }

    fun setComment() {
        viewModelScope.launch {
            apiRepository.setComment(commentRequest)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                        ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postFromDetailsMutable.postValue(
                            Response(
                                data = 0,
                                flag = 0
                            )
                        )
                    }

                }
        }
    }

    fun setLike() {
        viewModelScope.launch {
            apiRepository.postLike(likePostRequest)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        SUCCESS -> postFromDetailsMutable.postValue(
                            Response(
                                data = it.body()!!.like.postId,
                                flag = 0
                            )
                        )
                        ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.errorBody()}"))
                        AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                    }
                }
        }
    }

    fun removeLike() {
        viewModelScope.launch {
            apiRepository.removeLike(likePostRequest)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        SUCCESS -> postFromDetailsMutable.postValue(
                            Response(
                                data = "${likePostRequest.postId}",
                                flag = 0
                            )
                        )
                        ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.errorBody()}"))
                        AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                    }
                }
        }
    }

    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            apiRepository.deleteComment(comment.id.toString()).let {
                when (it.isRequestSuccess(it.body()!!.code)) {
                    AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                    ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.body()}"))
                    SUCCESS -> postFromDetailsMutable.postValue(
                        Response(
                            data = comment.postId,
                            flag = 0
                        )
                    )
                }

            }
        }
    }

    fun editComment(comment: Comment) {
        viewModelScope.launch {
            apiRepository.editComment(comment.id.toString(), editComment).let {
                when (it.isRequestSuccess(it.body()!!.code)) {
                    AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                    ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.body()}"))
                    SUCCESS -> postFromDetailsMutable.postValue(
                        Response(
                            data = comment.postId,
                            flag = 0
                        )
                    )
                }

            }
        }
    }

    fun postById2(id: String) {
        viewModelScope.launch {
            apiRepository.postById(id)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postStatusMutable.postValue(Response(reLogin = true))
                        ERROR -> postStatusMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postStatusMutable.postValue(
                            Response(
                                data = it.body()!!.data,
                                flag = 202
                            )
                        )
                    }
                }
        }
    }

    fun deletePost(post: PostsData) {
        viewModelScope.launch {
            apiRepository.deletePost(post.id.toString())
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                        ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postFromDetailsMutable.postValue(
                            Response(
                                data = post,
                                flag = -1
                            )
                        )
                    }
                }
        }
    }
    fun blockUser(post: PostsData) {
        viewModelScope.launch {
            apiRepository.blockUser(post.userId.toString())
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                        ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postFromDetailsMutable.postValue(
                            Response(
                                data = post,
                                flag = -2
                            )
                        )
                    }
                }
        }
    }

    fun deletePostImages(list: List<Int>) {
        viewModelScope.launch {
            apiRepository.deleteImages(DeleteImagesRequest(images = list))
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postStatusMutable.postValue(Response(reLogin = true))
                        ERROR -> postStatusMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postStatusMutable.postValue(
                            Response(
                                data = "$it",
                                flag = 101
                            )
                        )
                    }
                }
        }
    }

    fun postById(id: String) {
        viewModelScope.launch {
            apiRepository.postById(id)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postFromDetailsMutable.postValue(Response(reLogin = true))
                        ERROR -> postFromDetailsMutable.postValue(Response(error = "${it.body()}"))
                        SUCCESS -> postFromDetailsMutable.postValue(
                            Response(
                                data = it.body()!!.data,
                                flag = 1
                            )
                        )
                    }
                }
        }
    }

    val profileMutable: MutableLiveData<Response<Any?>> = MutableLiveData()
    fun getProfile() {
        viewModelScope.launch {
            apiRepository.getProfile()
                .let {
                    when (it.isRequestSuccess(it.body()?.code)) {
                        SUCCESS -> profileMutable.postValue(Response(it.body()!!))
                        ERROR -> profileMutable.postValue(Response(error = "${it.errorBody()}"))
                        AUT -> profileMutable.postValue(Response(reLogin = true))
                    }
                }
        }
    }


    fun uploadVideo(
        description: RequestBody,
        file: MultipartBody.Part
    ) {
        viewModelScope.launch {
            apiRepository.uploadVideo(description, file)
                .let {
                    when (it.isRequestSuccess(it.body()!!.code)) {
                        AUT -> postStatusMutable.postValue(Response(reLogin = true))
                        ERROR -> postStatusMutable.postValue(Response(error = "$it"))
                        SUCCESS -> postStatusMutable.postValue(
                            Response(
                                data = it.body()!!.data,
                                flag = 0
                            )
                        )
                    }
                }
        }
    }


}