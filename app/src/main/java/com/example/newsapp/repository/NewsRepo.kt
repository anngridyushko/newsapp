package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitNews
import com.example.newsapp.db.NewsDatabase
import com.example.newsapp.models.Article

class NewsRepo(val db: NewsDatabase) {

    suspend fun getNews(country: String, page: Int) =
        RetrofitNews.api.getNews(country, page)

    suspend fun searchNews(search: String, page: Int) =
        RetrofitNews.api.searchNews(search, page)

    suspend fun insertNewsDB(article: Article) =
        db.getArticleDAO().insertArticle(article)

    fun getFavoriteNews() =
        db.getArticleDAO().getArticles()

    suspend fun deleteArticle(article: Article) =
        db.getArticleDAO().deleteArticle(article)

}
