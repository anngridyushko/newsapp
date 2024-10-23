package com.example.newsapp.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.models.Article
import com.example.newsapp.models.NewsResponse
import com.example.newsapp.repository.NewsRepo
import com.example.newsapp.util.Constants
import com.example.newsapp.util.ResourceStatus
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class NewsViewModel(app: Application, val newsRepo: NewsRepo) : AndroidViewModel(app) {
    val news: MutableLiveData<ResourceStatus<NewsResponse>> = MutableLiveData()
    var newsPage = 1
    var newsResponse: NewsResponse? = null

    val search: MutableLiveData<ResourceStatus<NewsResponse>> = MutableLiveData()
    var searchPage = 1
    var searchResponse: NewsResponse? = null
    var searchQuery: String? = null
    var oldSearchQuery: String? = null

    init {
        getNews(Constants.COUNTRY_CODE)
    }

    fun getNews(countryCode: String) = viewModelScope.launch {
        newsInternet(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchInternet(searchQuery)
    }

    fun internetConnection(context: Context): Boolean {
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).apply {
            return getNetworkCapabilities(activeNetwork)?.run {
                when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } ?: false
        }
    }

    private suspend fun newsInternet(countryCode: String) {
        news.postValue(ResourceStatus.Loading())
        try {
            if (internetConnection((this.getApplication()))) {
                val response = newsRepo.getNews(countryCode, newsPage)
                news.postValue(handleNewsResponse(response))
            } else {
                news.postValue(ResourceStatus.Error("No Internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> news.postValue(ResourceStatus.Error("Unable to connect"))
                else -> news.postValue(ResourceStatus.Error("No signal"))
            }
        }
    }

    private suspend fun searchInternet(searchQuery: String) {
        this.searchQuery = searchQuery
        search.postValue(ResourceStatus.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRepo.searchNews(searchQuery, searchPage)
                search.postValue(handleSearchNewsResponse(response))
            } else {
                search.postValue(ResourceStatus.Error("No Internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> search.postValue(ResourceStatus.Error("Unable to connect"))
                else -> search.postValue(ResourceStatus.Error("No signal"))
            }
        }
    }

    private fun handleNewsResponse(response: Response<NewsResponse>): ResourceStatus<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                newsPage++
                if (newsResponse == null) {
                    newsResponse = result
                } else {
                    val oldArticles = newsResponse?.articles
                    val newArticles = result.articles
                    oldArticles?.addAll(newArticles)
                }
                return ResourceStatus.Success(newsResponse ?: result)

            }
        }
        return ResourceStatus.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): ResourceStatus<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (searchResponse == null || searchQuery != oldSearchQuery) {
                    searchPage = 1
                    oldSearchQuery = searchQuery
                    searchResponse = result
                } else {
                    searchPage++
                    val oldArticles = searchResponse?.articles
                    val newArticles = result.articles
                    oldArticles?.addAll(newArticles)
                }
                return ResourceStatus.Success(searchResponse ?: result)

            }
        }
        return ResourceStatus.Error(response.message())
    }

    fun addToFavourite(article: Article) = viewModelScope.launch {
        newsRepo.insertNewsDB(article)
    }

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepo.deleteArticle(article)
    }

    fun getFavouriteNews() = newsRepo.getFavoriteNews()


}