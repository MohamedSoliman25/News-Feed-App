package com.example.newsfeedapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.newsfeedapp.model.Article
import com.example.newsfeedapp.model.NewsResponse
import com.example.newsfeedapp.repository.NewsRepository
import com.example.newsfeedapp.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel (
        val app: Application,
        val newsRepository:NewsRepository
        ):ViewModel(){
    //for breaking news
    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingPageNumber = 1
//    var breakingNewsResponse:NewsResponse? = null

    //for searching news
    val searchNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPageNumber = 1

    init {
        getBreakingNews("eg")
    }

     fun getBreakingNews(countryCode: String) =viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingPageNumber)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

     fun getSearchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery,searchNewsPageNumber)
        searchNews.postValue(handleSearchNewsNewsResponse(response))
    }


        private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
            }
    }
        return Resource.Error(response.message())
}
//    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
//        if (response.isSuccessful){
//            response.body()?.let { resultResponse->
//                breakingPageNumber++
//                if (breakingNewsResponse ==null){
//                    breakingNewsResponse = resultResponse
//                }
//                else{
//                    val oldArticles = breakingNewsResponse?.articles
//                    val newArticles = resultResponse.articles
//                    oldArticles?.addAll(newArticles!!)
//                }
//                return Resource.Success(breakingNewsResponse?:resultResponse)
//            }
//    }
//        return Resource.Error(response.message())
//}

    private fun handleSearchNewsNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse->
                return Resource.Success(resultResponse)
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


}