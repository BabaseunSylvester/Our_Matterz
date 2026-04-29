package com.example.ourmatterz.data.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


data class ResponseModel (
    val status: String,
    val totalResults: Int,
    val articles: List<Article>?
)

@Entity (tableName = "articles")
data class Article (
    @Embedded val source: Source?,
    val author: String?,
    val title: String?,
    val description: String?,
    @PrimaryKey val url: String,
    val urlToImage: String?,
    val publishedAt: String?,
    val content: String?
)


data class Source (
    val id: String? = null,
    val name: String?
)