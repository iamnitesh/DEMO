package com.example.demo.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.apis.ApiResponse
import com.example.demo.db.AppDatabase
import com.example.demo.model.Photos
import com.example.demo.model.Posts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class DetailViewModel @Inject constructor (private val apiResponse: ApiResponse,
                                           private val database: AppDatabase
) : ViewModel() {


    private var postsList = MutableLiveData<List<Posts>>()
    val postsListLiveData: LiveData<List<Posts>>
    get() = postsList


    private var isLoading = MutableLiveData<Boolean>()
    val _isLoading: LiveData<Boolean>
    get() = isLoading


    private var error = MutableLiveData<String>("")
    val _error: LiveData<String>
    get() = error


    private var doApiCallAgain = true

    suspend fun getAllPosts(fromServer: Boolean,postID:Int) {

        try {
            isLoading.value = true

            val response = apiResponse.getPostDetails(postID)
            postsList.value = response
            isLoading.value = false

            if (response.isNullOrEmpty()) {
                doApiCallAgain = false
                postsList.value = emptyList()
                error.value = "RESPONSE IS EMPTY FROM SERVER"
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    database.photosDao().insertAllPosts(response)
                }
            }

        } catch (e: Exception) {
            isLoading.value = false
            error.value = "Something went Wrong ---  ${e.message} "
        }
    }
}