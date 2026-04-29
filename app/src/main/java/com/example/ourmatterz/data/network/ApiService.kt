package com.example.ourmatterz.data.network

import com.example.ourmatterz.data.models.ResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET ("everything")
    suspend fun searchNews(
        @Query("q") searchQuery: String,
        @Query("language") language: String = "en"
    ): Response<ResponseModel>

    @GET ("top-headlines")
    suspend fun getNews(
        @Query("category") category: String,
        @Query("pageSize") pageSize: String = "100",
    ): Response<ResponseModel>
}