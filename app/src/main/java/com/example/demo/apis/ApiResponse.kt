package com.example.demo.apis

import androidx.lifecycle.LiveData
import com.example.demo.model.Photos
import com.example.demo.model.Posts
import com.example.demo.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiResponse {


    @GET(Constants.END_POINT)
    suspend fun getPhotos(): List<Photos>

    @GET(Constants.END_POINT_DETAIL)
    suspend fun getPostDetails(@Query("postId") postID:Int): List<Posts>


}