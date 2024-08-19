package com.example.newsapp.api

import com.example.newsapp.models.NewsResponse
import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("v2/top-headlines")
    suspend fun getNews(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("country")
        country: String = "pl",
        @Query("page")
        page: Int = 1,
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("apiKey")
        apiKey: String = API_KEY,
        @Query("q")
        keywords: String,
        @Query("page")
        page: Int = 1,
    ): Response<NewsResponse>

}