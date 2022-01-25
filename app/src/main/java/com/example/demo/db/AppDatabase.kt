package com.example.demo.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.demo.dao.PhotosDao
import com.example.demo.model.Photos
import com.example.demo.model.Posts
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Singleton
@Database(
    entities = [
        Photos::class,
        Posts::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photosDao(): PhotosDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(app: Application): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(app).also { INSTANCE = it }
        }

        private fun buildDatabase(app: Application) =
            Room.databaseBuilder(
                app,
                AppDatabase::class.java,
                "photosDB"
            )
                .addCallback(object : Callback() {
                    // Pre-populate the database after onCreate has been called. If you want to prepopulate at opening time then override onOpen function instead of onCreate.
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Do database operations through coroutine or any background thread
                        val handler = CoroutineExceptionHandler { _, exception ->
                            println("Caught during database creation --> $exception")
                        }

                        CoroutineScope(Dispatchers.IO).launch(handler) {
                            //testing
                            prePopulateAppDatabase(getInstance(app).photosDao())
                        }
                    }
                })
                .build()

        suspend fun prePopulateAppDatabase(photos: PhotosDao) {
            val photoData = Photos(1, 1, "1", "Test", "");
            photos.insertPhotos(photoData)
        }
    }
}