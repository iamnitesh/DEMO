package com.example.demo.utils

import android.content.Context
import android.widget.Toast

object  Constants{

    const val BASE_URL = "https://jsonplaceholder.typicode.com"
    const val END_POINT = "/photos"
    const val END_POINT_DETAIL = "/comments?"



    fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, duration).show()
    }


}
