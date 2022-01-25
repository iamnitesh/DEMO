package com.example.demo.module

import com.example.demo.apis.ApiResponse
import com.example.demo.utils.Constants
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
 class NetworkModule {
    //https://jsonplaceholder.typicode.com/photos
    @Provides
    fun getBaseUrl(): String = Constants.BASE_URL

    @Provides
    fun getRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    fun getApis(retrofit: Retrofit):ApiResponse{
        return retrofit.create(ApiResponse::class.java)
    }


}