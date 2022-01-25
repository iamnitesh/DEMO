package com.example.demo.di

import android.app.Application
import com.example.demo.detail.DetailActivity
import com.example.demo.home.MainActivity
import com.example.demo.module.DatabaseModule
import com.example.demo.module.NetworkModule
import dagger.Component
import javax.inject.Singleton
import dagger.BindsInstance


@Singleton
@Component(modules = [NetworkModule::class,DatabaseModule::class])
interface  AppComponent {

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun application(application: Application): Builder
    }

    fun inject(mainActivity: MainActivity)
    fun inject(detailActivity: DetailActivity)


}