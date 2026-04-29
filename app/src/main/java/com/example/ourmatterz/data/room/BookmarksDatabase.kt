package com.example.ourmatterz.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ourmatterz.data.models.Article

@Database(entities = [Article::class], version = 2, exportSchema = false)
abstract class BookmarksDatabase : RoomDatabase() {

    abstract fun dao(): BookmarksDao

    companion object {
        @Volatile
        private var Instance: BookmarksDatabase? = null

        fun getDatabase(context: Context): BookmarksDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BookmarksDatabase::class.java, "bookmarks-database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}