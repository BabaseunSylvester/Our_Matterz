package com.example.ourmatterz.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.ourmatterz.data.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarksDao {
    @Insert
    suspend fun addArticle(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getArticles() : Flow<List<Article>>
}