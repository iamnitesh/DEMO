package com.example.demo.module

import android.app.Application
import com.example.demo.dao.PhotosDao
import com.example.demo.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import dagger.Binds





@Module
class DatabaseModule() {




    @Singleton
    @Provides
    fun provideDb(app:Application): AppDatabase {
        return AppDatabase.getInstance(app)
    }

    @Singleton
    @Provides
    fun provideLoginDao(db: AppDatabase): PhotosDao {
        return db.photosDao()
    }

}