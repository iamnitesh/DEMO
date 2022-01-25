package com.example.demo

import android.app.Application
import com.example.demo.di.AppComponent
import com.example.demo.di.DaggerAppComponent

class MyApplication : Application() {

    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
       appComponent =   DaggerAppComponent.builder().application(this).build()


    }


}