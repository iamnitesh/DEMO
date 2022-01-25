package com.example.demo.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.apis.ApiResponse
import com.example.demo.db.AppDatabase
import com.example.demo.model.Photos
import kotlinx.coroutines.*
import java.lang.Exception

import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val apiResponse: ApiResponse,
    private val database: AppDatabase
) : ViewModel() {


    private var photosList = MutableLiveData<List<Photos>>()
    val photosListLiveData: LiveData<List<Photos>>
        get() = photosList


    private var isLoading = MutableLiveData<Boolean>()
    val _isLoading: LiveData<Boolean>
        get() = isLoading


    private var error = MutableLiveData<String>("")
    val _error: LiveData<String>
        get() = error


    private var doApiCallAgain = true

    suspend fun getPhotos(fromServer: Boolean) {

        try {
            isLoading.value = true

            if (fromServer) {

                while (doApiCallAgain) {

                    val response = apiResponse.getPhotos()
                    photosList.value = response
                    isLoading.value = false

                    if (response.isNullOrEmpty()) {
                        doApiCallAgain = false
                        error.value = "RESPONSE IS EMPTY FROM SERVER"
                    } else {

                        viewModelScope.launch(Dispatchers.IO) {
                            database.photosDao().insertAllPhotos(response)
                        }

                    }
                    if (doApiCallAgain)
                        delay(1000 * 60 * 5)
                }


            } else {

                val response = viewModelScope.async(Dispatchers.IO) {
                    database.photosDao().getAllPhotos()
                }

                val res = response.await()
                photosList.value = res
                isLoading.value = false

                if (res.isEmpty()) {
                    error.value = "NO DATA FOUND IN DATABASE"
                }

            }

        } catch (e: Exception) {
            isLoading.value = false
            error.value = "Something went Wrong ---  ${e.message} "
        }
    }
}