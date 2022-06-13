package com.example.newsfeedapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.*
import com.example.newsfeedapp.model.Article
import com.example.newsfeedapp.model.NewsResponse
import com.example.newsfeedapp.repository.NewsRepository
import com.example.newsfeedapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel (
         val app: Application,
        val newsRepository:NewsRepository
        ):AndroidViewModel(app){
    //for breaking news
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber = 1
    var breakingNewsResponse:NewsResponse? = null

    //for searching news
    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPageNumber = 1
    var searchNewsResponse:NewsResponse? = null


    init {
        getBreakingNews("eg")
    }

     fun getBreakingNews(countryCode: String) =viewModelScope.launch {
//        breakingNews.postValue(Resource.Loading())
//        val response = newsRepository.getBreakingNews(countryCode, breakingPageNumber)
//        breakingNews.postValue(handleBreakingNewsResponse(response))
         safeBreakingNewsCall(countryCode)
    }

     fun getSearchNews(searchQuery:String) = viewModelScope.launch {
//        searchNews.postValue(Resource.Loading())
//        val response = newsRepository.searchNews(searchQuery,searchNewsPageNumber)
//        searchNews.postValue(handleSearchNewsNewsResponse(response))
         safeSearchNewsCall(searchQuery)
    }


//        private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
//        if (response.isSuccessful){
//            response.body()?.let { resultResponse->
//                return Resource.Success(resultResponse)
//            }
//    }
//        return Resource.Error(response.message())
//}
    // for pagination
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingPageNumber++
                if (breakingNewsResponse ==null){
                    breakingNewsResponse = resultResponse
                }
                else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles!!)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
    }
        return Resource.Error(response.message())
}

//    private fun handleSearchNewsNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
//        if (response.isSuccessful){
//            response.body()?.let { resultResponse->
//                return Resource.Success(resultResponse)
//            }
//        }
//        return Resource.Error(response.message())
//    }

    //for pagination
    private fun handleSearchNewsNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPageNumber++
                if (searchNewsResponse ==null){
                    searchNewsResponse = resultResponse
                }
                else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles!!)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun insertArticle(article: Article) = viewModelScope.launch {
        newsRepository.insert(article)
    }
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.delete(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()


    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.getBreakingNews(countryCode, breakingPageNumber)
                breakingNews.postValue(handleBreakingNewsResponse(response))            }
            else{
                breakingNews.postValue(Resource.Error("No Internet connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException ->breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNews.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){
                val response = newsRepository.searchNews(searchQuery,searchNewsPageNumber)
                searchNews.postValue(handleSearchNewsNewsResponse(response))
            }
            else{
                searchNews.postValue(Resource.Error("No Internet connection"))
            }

        }catch (t:Throwable){
            when(t){
                is IOException ->searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager =app.getSystemService(Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
                    ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true

                else -> false
            }
        }else{
           val activeNetworkInfo =  connectivityManager.activeNetworkInfo

            if(activeNetworkInfo!=null && activeNetworkInfo.isConnected){
                return true
            }
        }
        return false

    }
}