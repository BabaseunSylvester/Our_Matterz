package com.example.ourmatterz.data

import com.example.ourmatterz.data.models.Article
import com.example.ourmatterz.data.network.ApiService
import com.example.ourmatterz.data.room.BookmarksDao
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun searchNews(searchQuery: String): List<Article>?
    suspend fun getNews(category: String): List<Article>?

    suspend fun addArticle(article: Article)

    suspend fun deleteArticle(article: Article)

    fun getArticles(): Flow<List<Article>>
}



class AppRepository(
    private val apiService: ApiService,
    private val dao: BookmarksDao
) : Repository {

    override suspend fun searchNews(searchQuery: String): List<Article>? {
        val listToReturn = try {
            val response = apiService.searchNews(searchQuery)
            if (response.isSuccessful) {
                response.body()?.articles ?: emptyList()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return listToReturn
    }


    override suspend fun getNews(category: String): List<Article>? {
        val listToReturn = try {
            val response = apiService.getNews(category)
            if (response.isSuccessful) {
                response.body()?.articles ?: emptyList()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        return listToReturn
    }


    override suspend fun addArticle(article: Article) {
        dao.addArticle(article)
    }

    override suspend fun deleteArticle(article: Article) {
        dao.deleteArticle(article)
    }

    override fun getArticles(): Flow<List<Article>> {
        return dao.getArticles()
    }
}